package e.nick.billsplitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MoneySplit {

    private double total;
    private int payees;
    private double average;
    private Map<String, Double> changeFromAvg;
    private List<Double> debtors;
    private List<Double> creditors;
    private Map<Integer, String> debtorIndexes;
    private Map<Integer, String> creditorIndexes;
    private List<String> statements;

    public MoneySplit() {
        this.total = 0;
        this.payees = 0;
        this.changeFromAvg = new HashMap<>();
        this.debtorIndexes = new HashMap<>();
        this.creditorIndexes = new HashMap<>();
        this.statements = new ArrayList<>();
    }

    /**
     * Sets the total amount of people, the total payed by the group and the average cost per person
     *
     * @param funds
     */
    public void setTotal(Map<String, Double> funds) {
        this.payees = 0;
        for (String person : funds.keySet()) {
            this.total += funds.get(person);
            this.payees += 1;
        }
        this.average = this.total / (double) this.payees;
    }

    /**
     * Takes a map of people and what they have payed, and sets a map of those same people and how much off the average payment they are
     * Positive means person owes money (debtor), negative means they should receive money (creditor).
     *
     * @param map of <People, Payed> as a map <String, Double>
     */
    public void setToAverage(Map<String, Double> map) {
        for (String person : map.keySet()) {
            this.changeFromAvg.put(person, this.average - map.get(person));
        }
    }

    /**
     * Creates a list of debtors and creditors. Assigns their positions in a Map<Integer, String> to be retrieved later
     */
    public void setLists() {
        this.debtors = new ArrayList<>();
        this.creditors = new ArrayList<>();
        for (String person : this.changeFromAvg.keySet()) {
            if (this.changeFromAvg.get(person) > 0) {
                this.debtors.add(this.changeFromAvg.get(person));
                this.debtorIndexes.put(this.debtorIndexes.size(), person);
            } else if (this.changeFromAvg.get(person) < 0) {
                this.creditors.add(this.changeFromAvg.get(person));
                this.creditorIndexes.put(this.creditorIndexes.size(), person);
            }
        }
        Collections.sort(this.debtors);
        Collections.sort(this.creditors);
        Collections.reverse(this.creditors);
    }

    /**
     * Zeros the lists of debtors and creditors so that each person will have payed exactly the average.
     * Adds statements to List so they may be printed out client side to denote who owes who
     */
    public void zeroLists() {
        for (int i = 0; i < this.debtors.size(); i++) {
            for (int j = 0; j < this.creditors.size() && this.debtors.get(i) > 0; j++) {
                double payment = 0;
                String statement = "";
                double debt = this.debtors.get(i);
                double receive = this.creditors.get(j);
                if (this.debtors.get(i) + this.creditors.get(j) >= 0 && this.creditors.get(j) < 0) {
                    payment = Math.abs(this.creditors.get(j));
                    statement = this.debtorIndexes.get(i) + " owes " + this.creditorIndexes.get(j) + " " + payment;
                } else if (this.debtors.get(i) + this.creditors.get(j) < 0 && this.creditors.get(j) < 0) {
                    payment = this.debtors.get(i);
                    statement = this.debtorIndexes.get(i) + " owes " + this.creditorIndexes.get(j) + " " + payment;
                }
                if (!statement.equals("")) {
                    this.statements.add(statement);
                }
                this.debtors.set(i, debt - payment);
                this.creditors.set(j, receive + payment);
            }
        }
    }

    public Map<String, Double> getChangeFromAvg() {
        return this.changeFromAvg;
    }

    public double getAveragePayment() {
        return this.average;
    }

    public double getTotal() {
        return this.total;
    }

    public int getTotalPeople() {
        return this.payees;
    }

    public List<Double> getDebtors() {
        return this.debtors;
    }

    public List<Double> getCreditors() {
        return this.creditors;
    }

    public Map<Integer, String> getDebtorIndexes() {
        return this.debtorIndexes;
    }

    public Map<Integer, String> getCreditorIndexes() {
        return this.creditorIndexes;
    }

    public List<String> getStatements() {
        return this.statements;
    }
}