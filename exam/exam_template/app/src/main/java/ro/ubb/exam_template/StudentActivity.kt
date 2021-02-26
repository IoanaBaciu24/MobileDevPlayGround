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
import kotlinx.android.synthetic.main.student_section.*
import ro.ubb.exam_template.adaptor.MyAdapter
import ro.ubb.exam_template.model.MainModel
import ro.ubb.exam_template.util.logd

class StudentActivity : AppCompatActivity() {

    private var adapter: MyAdapter? = null
    private lateinit var model: MainModel
    private var wasOffline = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_section)
        model = ViewModelProvider(this).get(MainModel::class.java)
        model.getDrafts()
        draft_list.layoutManager = LinearLayoutManager(this)
        setupRecyclerView(draft_list)
        observeModel()

    }


    private fun observeModel() {
        model.loading.observe(this, Observer { displayLoading(it) })
        model.message.observe(this, Observer{ displayMessage(it) })

    }

    private fun displayLoading(loading: Boolean) {
        progress2.visibility = if (loading) View.VISIBLE else View.GONE
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

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = MyAdapter()
        model.entities.observe(this, Observer{ data ->
            if (data != null) {
                adapter!!.setData(data)
            }

        })
        adapter!!.app = application as App
        adapter!!.model= model
        adapter!!.context = this
        adapter!!.isDraft = true
        recyclerView.adapter = adapter
    }
}