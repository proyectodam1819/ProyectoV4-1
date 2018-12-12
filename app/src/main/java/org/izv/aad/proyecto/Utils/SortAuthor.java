package org.izv.aad.proyecto.Utils;

import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;

import java.util.Comparator;
import java.util.List;

public class SortAuthor implements Comparator<Book>{
    private Manager manager;
    private String n1;
    private String n2;

    public SortAuthor(Manager manager) {
        this.manager = manager;
    }

    @Override
    public int compare(Book o1, Book o2) {
        List<Author> authors = manager.getAllAuthor(null);
        for(int i = 0 ; i < authors.size() ; i++){
            if(authors.get(i).getId() == o1.getIdAuthor()){
                n1 = authors.get(i).getName();
            }
            if(authors.get(i).getId() == o2.getIdAuthor()){
                n2 = authors.get(i).getName();
            }
        }
        return n1.compareTo(n2);

    }
}
