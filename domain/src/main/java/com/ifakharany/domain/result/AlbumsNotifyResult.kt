package com.ifakharany.domain.result

sealed class AlbumsNotifyResult {
    object RecordsInserted: AlbumsNotifyResult()
    object Error: AlbumsNotifyResult()
}