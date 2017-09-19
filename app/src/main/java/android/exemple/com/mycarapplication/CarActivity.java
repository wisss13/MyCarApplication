package android.exemple.com.mycarapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.exemple.com.mycarapplication.domain.Car;
import android.exemple.com.mycarapplication.extras.DataUrl;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.mikepenz.materialdrawer.Drawer;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


public class CarActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Car car;
    private Drawer.Result navigationDrawerLeft;
    private MaterialDialog mMaterialDialog;
    private TextView tvDescription;
    private ViewGroup mRoot;

    private TextView tvTestDrive;
    private Button btTestDrive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TRANSITIONS
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                /*Explode trans1 = new Explode();
                trans1.setDuration(3000);
                Fade trans2 = new Fade();
                trans2.setDuration(3000);

                getWindow().setEnterTransition( trans1 );
                getWindow().setReturnTransition( trans2 );*/

                TransitionInflater inflater = TransitionInflater.from( this );
                Transition transition = inflater.inflateTransition( R.transition.transitions );

                getWindow().setSharedElementEnterTransition(transition);

                Transition transition1 = getWindow().getSharedElementEnterTransition();
                transition1.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        TransitionManager.beginDelayedTransition(mRoot, new Slide());
                        tvDescription.setVisibility( View.VISIBLE );
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });
            }

        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_car);

        if (savedInstanceState != null) {
            car = savedInstanceState.getParcelable("car");
        } else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getParcelable("car") != null) {
                car = getIntent().getExtras().getParcelable("car");
            } else {
                Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle( car.getModel() );

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle(car.getModel());
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        tvTestDrive = (TextView) findViewById(R.id.tv_test_drive);
        btTestDrive = (Button) findViewById(R.id.bt_test_drive);
        btTestDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleTestDrive(v);
            }
        });

        mRoot = (ViewGroup) findViewById(R.id.ll_tv_description);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        ImageView ivCar = (ImageView) findViewById(R.id.iv_car);
        //SimpleDraweeView ivCar = (SimpleDraweeView) findViewById(R.id.iv_car);
        TextView tvModel = (TextView) findViewById(R.id.tv_model);
        TextView tvBand = (TextView) findViewById(R.id.tv_brand);
        Button btPhone = (Button) findViewById(R.id.bt_phone);
        btPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog = new MaterialDialog(new ContextThemeWrapper(CarActivity.this, R.style.MyAlertDialog))
                        .setTitle("Telephone")
                        .setMessage(car.getTel())
                        .setPositiveButton("Joindre", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = new Intent(Intent.ACTION_CALL);
                                it.setData(Uri.parse("tel:" + car.getTel().trim()));
                                startActivity(it);
                            }
                        })
                        .setNegativeButton("Retour", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });
                mMaterialDialog.show();
            }
        });

    /*    Uri uri = Uri.parse( "https://lh6.googleusercontent.com/-WRJcLJZJspo/VVtjFVRucBI/AAAAAAADT-w/NnPHX5__C_s/w426-h295/descendo_com_Estilo.gif");
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri( uri )
                .setAutoPlayAnimations(true)
                .setOldController( ivCar.getController() )
                .build();

        ivCar.setController( dc );*/

        ivCar.setImageResource(car.getPhoto());
        tvModel.setText(car.getModel());
        tvBand.setText(car.getBrand());
        tvDescription.setText(car.getDescription());

        navigationDrawerLeft = new Drawer()
            .withActivity(this)
            .withToolbar(mToolbar)
            .withActionBarDrawerToggle(false)
            .withCloseOnClick(true)
            .withActionBarDrawerToggleAnimated(true)
                .withActionBarDrawerToggle(new ActionBarDrawerToggle(this, new DrawerLayout(this), R.string.drawer_open, R.string.drawer_close) {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        super.onDrawerSlide(drawerView, slideOffset);
                        navigationDrawerLeft.closeDrawer();
                        finish();
                    }
                })

            .build();

        // FAB
        //Keytool -list -keystore /Users/lenovo/.android/debug.keystore -alias anfroiddebugkey -storepass android-keypass android
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(CarActivity.this, "FABClicked.", Toast.LENGTH_SHORT).show();
                    InviteCall();
                }
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MainActivity.REQUEST_INVITE){
            if(resultCode == RESULT_OK){
                String ids[] = AppInviteInvitation.getInvitationIds(resultCode, data);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView(item);
        }

        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle onState){
      super.onSaveInstanceState(onState);
        onState.putParcelable("car", car);
    }

    @Override
    public void onBackPressed(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            TransitionManager.beginDelayedTransition(mRoot, new Slide());
            tvDescription.setVisibility( View.VISIBLE );
        }

            super.onBackPressed();
    }

    private void InviteCall(){
        Intent intent = new AppInviteInvitation.IntentBuilder("MyCarApplication")
                .setMessage("You need to download this APP, it's great!")
                .setDeepLink(Uri.parse(car.getBrand() + "/" + car.getModel()))
                .build();

        startActivityForResult(intent, MainActivity.REQUEST_INVITE);
    }

    // TEST DRIVE
    private int year, month, day, hour, minute;

    public void scheduleTestDrive(View view) {
        initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        Calendar cMin = Calendar.getInstance();
        Calendar cMax = Calendar.getInstance();
        cMax.set( cMax.get(Calendar.YEAR), 11, 31 );
        datePickerDialog.setMinDate(cMin);
        datePickerDialog.setMaxDate(cMax);

        List<Calendar> daysList = new LinkedList<>();
        Calendar[] daysArray;
        Calendar cAux = Calendar.getInstance();

        while( cAux.getTimeInMillis() <= cMax.getTimeInMillis() ){
            if( cAux.get( Calendar.DAY_OF_WEEK ) != 1 && cAux.get( Calendar.DAY_OF_WEEK ) != 7 ){
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis( cAux.getTimeInMillis() );

                daysList.add( c );
            }
            cAux.setTimeInMillis( cAux.getTimeInMillis() + ( 24 * 60 * 60 * 1000 ) );
        }
        daysArray = new Calendar[ daysList.size() ];
        for( int i = 0; i < daysArray.length; i++ ){
            daysArray[i] = daysList.get(i);
        }

        datePickerDialog.setSelectableDays( daysArray );
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show( getFragmentManager(), "DatePickerDialog" );
    }

    private void initDateTimeData(){
        if( year == 0 ){
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        year = month = day = hour = minute = 0;
        tvTestDrive.setText("");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, month, day, hour, minute);

        year = i;
        month = i1;
        day = i2;

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                tDefault.get(Calendar.HOUR_OF_DAY),
                tDefault.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setOnCancelListener(this);
        timePickerDialog.show(getFragmentManager(), "timePickerDialog");
        timePickerDialog.setTitle("Schedule Test Drive");

        timePickerDialog.setThemeDark(true);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
        if( i < 9 || i > 18 ){
            onDateSet(null, year, month, day);
            Toast.makeText(this, "Only between 9h e 18h", Toast.LENGTH_SHORT).show();
            return;
        }

        hour = i;
        minute = i1;

        tvTestDrive.setText( (day < 10 ? "0"+day : day)+"/"+
                (month+1 < 10 ? "0"+(month+1) : month+1)+"/"+
                year+" at "+
                (hour < 10 ? "0"+hour : hour)+"h"+
                (minute < 10 ? "0"+minute : minute));
    }

}



