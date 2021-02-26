package ro.ubb.exam_template.adaptor

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.ubb.exam_template.App
import ro.ubb.exam_template.R
import ro.ubb.exam_template.domain.Entite
import ro.ubb.exam_template.model.MainModel

class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>()
{
    private var data = mutableListOf<Entite>()
    var isDraft= false
    var model: MainModel? = null
    var app: App? = null
    var context: Context?=null


    fun setData(books: List<Entite>) {
        data.clear()
        data.addAll(books)
        notifyDataSetChanged()
    }



    inner class ViewHolder internal constructor(internal val mView: View) : RecyclerView.ViewHolder(mView) {
        internal val mIdView: TextView = mView.findViewById(R.id.id)
        internal val mContentView: TextView = mView.findViewById(R.id.content)
        internal var mItem: Entite? = null

        override fun toString(): String {
            return "${super.toString()} '${mContentView.text}'"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_component, parent, false)
        return ViewHolder(view)    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicle = data[position]
        holder.mItem = vehicle
        holder.mIdView.text = vehicle.id.toString()

        if(isDraft)
        {
            holder.mContentView.text = vehicle.name + " " + vehicle.students

            holder.mContentView.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Do you want to join the exam?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database

                        model?.joinExam(vehicle.id)
                        model!!.getDrafts()
//                    startActivity(Intent(this, MainActivity::class.java))

                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()

            }




        }
        else{
            holder.mContentView.text = vehicle.name + " " + vehicle.group+" " + vehicle.type
            holder.mContentView.setOnClickListener()
            {
                model?.getExamById(vehicle.id)
            }
        }

    }
}