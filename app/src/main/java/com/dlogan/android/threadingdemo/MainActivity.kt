package com.dlogan.android.threadingdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Flowable
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.disposables.CompositeDisposable
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    lateinit var compositeDisposable: CompositeDisposable


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compositeDisposable = CompositeDisposable()

    }

    override fun onResume() {
        super.onResume()

        compositeDisposable.add(
            giveMeNumbers().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ num ->
                    tv1.setText(num.toString())
                }, { e ->
                })
        )
    }

    override fun onPause() {
        super.onPause()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun giveMeNumbers(): Flowable<Long> {
        return Flowable.create<Long>({ subscriber ->
            var state: Long = 0
            while (!subscriber.isCancelled) {
                subscriber.onNext(Random.nextLong())
            }
        }, BackpressureStrategy.DROP)
    }

}
