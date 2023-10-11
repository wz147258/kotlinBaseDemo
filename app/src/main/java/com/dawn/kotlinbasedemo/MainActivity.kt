package com.dawn.kotlinbasedemo

import android.os.Bundle
import androidx.lifecycle.flowWithLifecycle
import com.dawn.kotlinbasedemo.databinding.ActivityMainBinding
import com.dawn.kotlinbasedemo.vm.MainVm
import com.dawn.lib_base.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding, MainVm>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
//        viewModel.requestData();
        vm.requestFlowData()
//        viewModel.requestLoopData();
//        viewModel.requestWithTake()
//        suspendCancellableCoroutine {
//
//        }
        MainScope().launch {
            flow<String> {

            }.flowOn(Dispatchers.IO)
//                .stateIn()
//                .shareIn()
                .flowWithLifecycle(lifecycle)
                .collect {

                }
        }
    }

    override fun getVariableId(): Int {
        return BR.mainVm

    }
}