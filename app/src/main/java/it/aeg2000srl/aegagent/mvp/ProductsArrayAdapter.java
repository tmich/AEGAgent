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
import it.aeg2000srl.aegagent.core.Product;

/**
 * Created by tiziano.michelessi on 29/09/2015.
 */
public class ProductsArrayAdapter extends ArrayAdapter<Product> {
    private final Activity context;
    private final List<Product> products;

    public ProductsArrayAdapter(Activity context, List<Product> products) {
        super(context, R.layout.products, products);
        this.context = context;
        this.products = products;
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public TextView textView;
        public TextView idView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder will buffer the assess to the individual fields of the row
        // layout

        ViewHolder holder;
        // Recycle existing productsView if passed as parameter
        // This will save memory and time on Android
        // This only works if the base layout for all classes are the same
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.products, null, true);
            holder = new ViewHolder();
            holder.textView = (TextView) rowView.findViewById(R.id.txtName);
            holder.idView = (TextView) rowView.findViewById(R.id.txtId);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textView.setText(products.get(position).getName());
        holder.idView.setText(String.valueOf(products.get(position).getId()));

//        String s = products.get(position).getName();

        return rowView;
    }
}
