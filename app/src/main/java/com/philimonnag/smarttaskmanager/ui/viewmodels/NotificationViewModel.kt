package com.philimonnag.smarttaskmanager.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel:ViewModel() {
    private  val _isNotificationOn =MutableLiveData(false)
    val isNotificationOn:LiveData<Boolean> = _isNotificationOn
    fun toggleNotification(){
        _isNotificationOn.value=_isNotificationOn.value?.not()
    }
}