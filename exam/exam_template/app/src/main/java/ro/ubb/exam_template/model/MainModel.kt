package ro.ubb.exam_template.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.ubb.exam_june192020.service.Service
import ro.ubb.exam_june192020.service.ServiceFactory
import ro.ubb.exam_template.App
import ro.ubb.exam_template.domain.Entite
import ro.ubb.exam_template.util.logd

class MainModel : ViewModel()
{
    private val service: Service = ServiceFactory
        .createRetrofitService(Service::class.java, Service.SERVICE_ENDPOINT)


    private val mutableEntities = MutableLiveData<List<Entite>>().apply { value = emptyList() }
    private val mutableExam = MutableLiveData<Entite>().apply { value = Entite(0, "", "", "", "", 0, "") }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableMessage = MutableLiveData<String>()

    val entities: LiveData<List<Entite>> = mutableEntities
    val loading: LiveData<Boolean> = mutableLoading
    val message: LiveData<String> = mutableMessage
    val exam: LiveData<Entite> = mutableExam
    fun isOnline(context: Context): Boolean{
        logd("checking internet")
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        cm.apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    fun fetchDataFromNetwork(app: App) {
        logd("getting data from network")
        viewModelScope.launch {
            mutableLoading.value = true
            try {
                println("GOT HERE")
                mutableEntities.value = service.getData()
                GlobalScope.launch(Dispatchers.IO) {
                    app.db.entDAO.deleteAll()
                    app.db.entDAO.addEntities(entities.value!!)
                }
            } catch (e: Exception) {
                mutableMessage.value = "Received an error while retrieving the data: ${e.message}"
            } finally {
                mutableLoading.value = false
            }
        }
    }

    fun fetchData(app: App) {
        logd("getting data either from server or db")
        mutableLoading.value = true
        try {
            println("AM AJUNS AICI!!")
            GlobalScope.launch(Dispatchers.IO) {
                val numberOfBooks = app.db.entDAO.numberOfEntities
                println(numberOfBooks)
                if (numberOfBooks <= 0) {
                    fetchDataFromNetwork(app)
                }

            }
        } catch (e: Exception) {
            mutableMessage.value = "Received an error while retrieving local data: ${e.message}"
        } finally {
            mutableLoading.value = false

        }
    }

    fun getExamById(id: Int)
    {
        logd("getting exam by id: " + id)
        viewModelScope.launch {
            mutableLoading.value = true
            try {
                println("GOT HERE")
                mutableExam.value = service.getExamById(id)

            } catch (e: Exception) {
                mutableMessage.value = "Received an error while retrieving the data: ${e.message}"
            } finally {
                mutableLoading.value = false
            }
        }
    }

    fun addEntity(app: App, entity: Entite, context: Context) {
        logd("add a new exam " + entity.name + " " +entity.type)
        mutableLoading.value = true
        viewModelScope.launch {
            try {
                if(isOnline(context)) {
                    launch(Dispatchers.IO) {
                        logd("adding online")
                        service.addData(entity)
                        println("adding entity...")
                        val addBooks = app.db.entDAO.addEntities(mutableListOf(entity))
                        println(addBooks.size)
                    }

                }
                else
                {
                    logd("adding offline")
                    GlobalScope.launch(Dispatchers.IO) {
                        val addBooks = app.db.entDAO.addEntities(mutableListOf(entity))

                    }
                    mutableMessage.value = "this is only in the db"
                }

//                launch(Dispatchers.IO) {
//                }
            } catch (e: Exception) {
                mutableMessage.value = "Received an error while adding the data: ${e.message}"
                println(e)
            } finally {
                mutableLoading.value = false
            }
        }
    }

    fun getDrafts() {
        logd("getting drafts from network")
        viewModelScope.launch {
            mutableLoading.value = true
            try {
                println("GOT HERE")
                mutableEntities.value = service.getDraft()

            } catch (e: Exception) {
                mutableMessage.value = "Received an error while retrieving the data: ${e.message}"
            } finally {
                mutableLoading.value = false
            }
        }
    }


    fun joinExam(id:Int)
    {
        logd("joining the exam with id: " + id)
        viewModelScope.launch {
            mutableLoading.value = true
            try {
                println("GOT HERE")
                service.joinExam(Entite(id, "", "", "", "", 0,"" ))

            } catch (e: Exception) {
                mutableMessage.value = "Received an error while retrieving the data: ${e.message}"
            } finally {
                mutableLoading.value = false
            }
        }
    }

    fun getGroup()
    {
        logd("getting groups from network")
        viewModelScope.launch {
            mutableLoading.value = true
            try {
                println("GOT HERE")
                mutableEntities.value = service.getGroup()

            } catch (e: Exception) {
                mutableMessage.value = "Received an error while retrieving the data: ${e.message}"
            } finally {
                mutableLoading.value = false
            }
        }
    }


}