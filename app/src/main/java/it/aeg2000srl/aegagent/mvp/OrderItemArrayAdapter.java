package it.aeg2000srl.aegagent.mvp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import it.aeg2000srl.aegagent.R;

/**
 * Created by tiziano.michelessi on 02/10/2015.
 */
public class OrderItemArrayAdapter extends ArrayAdapter<OrderItemViewModel> {
    private final Activity context;
    private final List<OrderItemViewModel> items;

    public OrderItemArrayAdapter(Activity context, List<OrderItemViewModel> items) {
        super(context, R.layout.products, items);
        this.context = context;
        this.items = items;
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

        holder.textView.setText(items.get(position).Product.Name);
        holder.idView.setText(String.valueOf(items.get(position).Quantity));
        return rowView;
    }
}
