package pt.ulisboa.tecnico.cmov.frontend

import android.app.Application
import pt.ulisboa.tecnico.cmov.frontend.data.AppContainer
import pt.ulisboa.tecnico.cmov.frontend.data.DefaultAppContainer

class PharmacISTApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}