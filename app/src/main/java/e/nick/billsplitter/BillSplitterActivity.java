package e.nick.billsplitter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillSplitterActivity extends AppCompatActivity {


    private MoneySplit moneySplit;
    private int personCount = 1;
    private List<EditText> allEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_splitter_layout);
        moneySplit = new MoneySplit();
        allEditText = new ArrayList<>();
        EditText name1 = findViewById(R.id.name1);
        EditText value1 = findViewById(R.id.money1);
        EditText name2 = findViewById(R.id.name2);
        EditText value2 = findViewById(R.id.money2);
        allEditText.add(name1);
        allEditText.add(value1);
        allEditText.add(name2);
        allEditText.add(value2);
    }

    /*
    Creates 2 new EditText fields. First one to hold a name, second one to hold dollar value
    Potentially use linear layout - may prevent the need for constraints
     */
    public void addPerson(View V) {
        try {
            EditText editTextNewName = new EditText(this);
            EditText editTextNewValue = new EditText(this);
            editTextNewName.setHint(R.string.name);
            editTextNewValue.setHint(R.string.dollar_amount);
            editTextNewName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            editTextNewValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            LinearLayout linearLayout = findViewById(R.id.entryLinearLayout);
            linearLayout.addView(editTextNewName);
            linearLayout.addView(editTextNewValue);

            editTextNewName.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            editTextNewValue.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            allEditText.add(editTextNewName);
            allEditText.add(editTextNewValue);
        }
        catch(Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }


    /*
    Grabs all info from EditText fields and will pass them to MoneySplit to calculate and display owing statements
    Requires a map in the form of <Name, Funds>, passed as a map <String, Double>
    Maybe use a list of EditTexts, already initialized with starting 2 EditText fields.
    Every added EditText should then be added to the list
    a for loop over the list can then pass the text from within the EditText to a String Array or String List
     */
    public void calculate(View v) {
        try {
            ((TextView) findViewById(R.id.statements)).setText(""); // reset text box for multiple clicks
            List<String> names = new ArrayList<>();
            List<Double> values = new ArrayList<>();
            Map<String, Double> map = new HashMap<>();
            Pattern pattern = Pattern.compile("^[a-zA-Z]+");
            for (EditText editText : allEditText) {
                String enteredText = editText.getText().toString().trim();
                Matcher matcher = pattern.matcher(enteredText);
                if (!matcher.matches()) {
                    String noLeadingZeroes = enteredText.replaceFirst("^0*", ""); //causes problems if only 0 is entered, below if statement fixes problem
                    if (noLeadingZeroes.equals("")) noLeadingZeroes = "0";
                    values.add(Double.parseDouble(noLeadingZeroes));
                } else {
                    names.add(enteredText);
                }
            }
            for (int i = 0; i < names.size(); i++) {
                map.put(names.get(i), values.get(i));
            }
            moneySplit.setTotal(map);
            moneySplit.setToAverage(map);
            moneySplit.setLists();
            moneySplit.zeroLists();
            List<String> statements = new ArrayList<>(moneySplit.getStatements());
            String listOfPayments = "";
            for (String s : statements) {
                listOfPayments += s + ((TextView) findViewById(R.id.statements)).getText().toString().trim() + "\n";
            }
            ((TextView) findViewById(R.id.statements)).setText(listOfPayments);
        } catch(Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
