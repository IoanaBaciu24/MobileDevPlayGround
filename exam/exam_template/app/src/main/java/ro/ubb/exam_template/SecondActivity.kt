package ro.ubb.exam_template

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.second_screen.*
import ro.ubb.exam_june192020.service.NetworkConnectionService
import ro.ubb.exam_template.adaptor.MyAdapter
import ro.ubb.exam_template.domain.Entite
import ro.ubb.exam_template.model.MainModel
import ro.ubb.exam_template.util.logd

class SecondActivity : AppCompatActivity() {

    private var adapter: MyAdapter? = null
    private lateinit var model: MainModel
    private var wasOffline = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_screen)

        model = ViewModelProvider(this).get(MainModel::class.java)
        model.fetchData(application as App)
        main_list.layoutManager = LinearLayoutManager(this)
        setupRecyclerView(main_list)
        observeModel()
        observeExam()
        val networkConnection = NetworkConnectionService(applicationContext);
        networkConnection.observe(this, Observer {isConnected ->
            if(isConnected){
                if(wasOffline) {

//                    performServerUpdate()
//                    (application as App).db.localVehicleDAO.deleteVehicles()
                    model.fetchDataFromNetwork(application as App)
                    Toast
                        .makeText(
                            this,
                            "Device is back online",
                            Toast.LENGTH_LONG
                        )
                        .show()
                    wasOffline = false
                }
            }
            else{
                wasOffline = true;
                Toast
                    .makeText(this,
                        "Device is offline. Some features will not be available",
                        Toast.LENGTH_LONG)
                    .show()
            }
        })

        post_button.setOnClickListener()
        {
            var students = students_edit.text.toString()
            var nr = 0
            if(students!="")
                nr = students.toInt()

            model.addEntity(application as App, Entite(0, name_edit.text.toString(),group_edit.text.toString(), details_edit.text.toString(), status_edit.text.toString(),nr ,type_edit.text.toString() ), this)
        }
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = MyAdapter()
        (application as App).db.entDAO.entities.observe(this, Observer{ data ->
            if (data != null) {
                adapter!!.setData(data)
            }

        })
        adapter!!.app = application as App
        adapter!!.model= model
        recyclerView.adapter = adapter
    }

    private fun observeModel() {
        model.loading.observe(this, Observer { displayLoading(it) })
        model.message.observe(this, Observer{ displayMessage(it) })

    }

    private fun displayLoading(loading: Boolean) {
        progress1.visibility = if (loading) View.VISIBLE else View.GONE
    }
    private fun displayMessage(message: String?) {
        if (message == null)
            return
        if (message.isNotBlank()) {
//            toast(message)
            logd(message)

            Toast
                    .makeText(this,
                            message,
                            Toast.LENGTH_LONG)
                    .show()
        }
    }

    private fun observeExam()
    {
        model.exam.observe(this, Observer{
            println(it)
            if(it!=null)
            {
                details_text_view.setText(it.details)
            }
        })
    }
}