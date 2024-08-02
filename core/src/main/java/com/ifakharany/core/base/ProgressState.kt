package com.ifakharany.core.base

sealed class ProgressState{
    
    data object Loading: ProgressState()

    data object Gone: ProgressState()
}