package com.example.cleanarhitecturewithmvvm.domain.type.insert

interface InsertUseCase<P> {
    suspend fun execute(store:P)
}