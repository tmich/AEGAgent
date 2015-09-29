package it.aeg2000srl.aegagent.mvp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import it.aeg2000srl.aegagent.R;

public class HomeActivity extends AppCompatActivity implements IHomeView {

    // UI references
    //Button btnUpdate;
    Button btnGoToCustomers;
    Button btnGoToProducts;

    HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnGoToCustomers = (Button)findViewById(R.id.btnGoToCustomers);
        btnGoToProducts = (Button)findViewById(R.id.btnGoToProducts);

//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View productsView) {
//                presenter.onUpdateButtonClick();
//            }
//        });
//
//        btnGoToCustomers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View productsView) {
//                presenter.onGoToCustomersButtonClick();
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter = new HomePresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void update() {

    }

    @Override
    public void onGoToCustomers(View.OnClickListener listener) {
        btnGoToCustomers.setOnClickListener(listener);
    }

    @Override
    public void onGoToProducts(View.OnClickListener listener) {
        btnGoToProducts.setOnClickListener(listener);
    }
}
