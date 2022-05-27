package kz.btsd.containers

interface iContainer {
    fun isCreated(): Boolean
    fun start()
    fun stop()
    fun getPort(): Int
}