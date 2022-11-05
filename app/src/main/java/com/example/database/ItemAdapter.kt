package com.example.database

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.database.databinding.ItemsRowBinding

/* Create the basic adapter extending from RecyclerView.Adapter
 Note that we specify the custom ViewHolder which gives us access to our views */
class ItemAdapter(
    val context: Context,
    val items: ArrayList<EmpModelClass>
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    /* Provide a direct reference to each of the views within a data item
     Used to cache the views within the item layout for fast access */
    inner class ViewHolder(val binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        /* Your holder should contain and initialize a member variable
         for any view that will be set as you render a row */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemsRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.binding.tvItemName.text = item.name
        holder.binding.tvItemEmail.text = item.email

        if (position % 2 == 0) {
            holder.binding.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGrey

                )
            )
        } else {
            holder.binding.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.binding.ivEdit.setOnClickListener { view ->

            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }

        }

        holder.binding.ivDelete.setOnClickListener { view ->

            if (context is MainActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}