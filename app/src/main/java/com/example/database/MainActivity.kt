package com.example.database

import android.app.Dialog
import android.os.Build.VERSION_CODES.Q
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.database.databinding.ActivityMainBinding
import com.example.database.databinding.DialogUpdateBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var binding2: DialogUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddRecord.setOnClickListener { view ->

            addRecord(view)
        }

        // Sets up the recycler view right after the app starts.
        setupListOfDataIntoRecyclerView()
    }

    //Method for saving records in database
    fun addRecord(view: View) {
        val name = binding.etName.text.toString()
        val email = binding.etEmailId.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        if (!name.isEmpty() && !email.isEmpty()) {
            val status =
                databaseHandler.addEmployee(EmpModelClass(0, name, email))
            // Checking if the long value returned is right if it is less than or equal to -1
            //something went wrong.
            if (status > -1) {
                Toast.makeText(applicationContext, "Record Saved", Toast.LENGTH_LONG).show()
                binding.etName.text.clear()
                binding.etEmailId.text.clear()

                setupListOfDataIntoRecyclerView()
            }
        } else {
            Toast.makeText(applicationContext, "Name or Email cannot be blank", Toast.LENGTH_LONG)
                .show()
        }
    }

    /*
    Function is used to show the list of inserted data.
     */
    private fun setupListOfDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            binding.rvItemsList.visibility = View.VISIBLE

            //Set The LayoutManager That This RecyclerView Will Use.
            binding.rvItemsList.layoutManager = LinearLayoutManager(this)
            // Adapter Class is initialized and list is passed into the parameter.
            val itemAdapter = ItemAdapter(this, getItemsList())
            // Adapter instance is set to the recyclerView to inflate the items.
            binding.rvItemsList.adapter = itemAdapter
        } else {

            binding.rvItemsList.visibility = View.GONE
        }
    }

    /*
    Function is used to get the Items List which is added in the database.
     */
    private fun getItemsList(): ArrayList<EmpModelClass> {
        //creating the instance of DatabaseHandler Class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        return databaseHandler.viewEmployee()
    }

    /*
    Method is used to show the Custom Dialog.
     */
    fun updateRecordDialog(empModelClass: EmpModelClass) {
        val updateDialog = Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog_MinWidth)
        updateDialog.setCancelable(false)
        /*
        Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.
         */
        binding2 = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding2.root)




        binding2.etDialogName.setText(empModelClass.name)

        binding2.etDialogEmailId.setText(empModelClass.email)


        binding2.btnDialogUpdate.setOnClickListener {
            val name = binding2.etDialogName.text.toString()
            val email = binding2.etDialogEmailId.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!name.isEmpty() && !email.isEmpty()) {
                val status =
                    databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Records Updated!", Toast.LENGTH_LONG).show()

                    setupListOfDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be killed.
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be empty.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        binding2.btnDialogCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        //Start the dialog and display it on screen.
        updateDialog.show()


    }

    /*
    Method is used to show the Alert Dialog.
     */
    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set Message for alert dialog
        builder.setMessage("Are you sure you want to delete ${empModelClass.name}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //Performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", ""))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Record Deleted Successfully",
                    Toast.LENGTH_LONG,
                ).show()

                setupListOfDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog kill.
        }

        //Performing Negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // kill dialog
        }
        //Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        //set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to dismiss dialog,with click on back btn
        alertDialog.show() //Show the dialog to UI
    }
}
