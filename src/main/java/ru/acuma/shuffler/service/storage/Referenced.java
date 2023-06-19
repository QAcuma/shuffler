package ru.acuma.shuffler.service.storage;

public interface Referenced<T> {

    T getReference(Long id);
}
