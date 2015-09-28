package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.aeg2000srl.aegagent.R;
import it.aeg2000srl.aegagent.core.Customer;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class CustomersArrayAdapter extends ArrayAdapter<Customer> {
    private final Activity context;
    private final List<Customer> customers;

    public CustomersArrayAdapter(Activity context, List<Customer> customers) {
        super(context, R.layout.customers, customers);
        this.context = context;
        this.customers = customers;
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ImageButton ibConfirm;
        public ImageButton ibNotNow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder will buffer the assess to the individual fields of the row
        // layout

        ViewHolder holder;
        // Recycle existing view if passed as parameter
        // This will save memory and time on Android
        // This only works if the base layout for all classes are the same
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.customers, null, true);
            holder = new ViewHolder();
            holder.textView = (TextView) rowView.findViewById(R.id.txtName);
            holder.imageView = (ImageView) rowView.findViewById(R.id.icon);
            //holder.ibConfirm = (ImageButton) rowView.findViewById(R.id.ibNotNow);
            //holder.ibNotNow= (ImageButton) rowView.findViewById(R.id.ibNotNow);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textView.setText(customers.get(position).getName());
        // Change the icon for Windows and iPhone
        String s = customers.get(position).getName();
//        holder.imageView.setImageResource(android.support.v7.appcompat.R.drawable.ic);

        return rowView;
    }
}

