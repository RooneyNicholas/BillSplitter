package e.nick.billsplitter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class BillSplitterActivity extends AppCompatActivity {


    private MoneySplit moneySplit;
    private int personCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_splitter_layout);
        moneySplit = new MoneySplit();
    }

    /*
    Creates 2 new EditText fields. First one to hold a name, second one to hold dollar value
     */
    public void addPerson(View V) {
    }


    /*
    Grabs all info from EditText fields and will pass them to MoneySplit to calculate and display owing statements
    Requires a map in the form of <Name, Funds>, passed as a map <String, Double>
     */
    public void calculate(View v) {
        Map<String, Double> nameToFunds = new HashMap<>();
        ConstraintLayout cl = new ConstraintLayout(this);
        
    }
}
