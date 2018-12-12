package org.izv.aad.proyecto.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.InterfaceFireBase;
import org.izv.aad.proyecto.Interfaces.OnItemClickListener;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;

import java.util.List;

public class AdapterIndex extends RecyclerView.Adapter <AdapterIndex.MyViewHolder> {

    private List<Book> books;
    private OnItemClickListener listener;
    private Manager manager;
    private Context context;
    private InterfaceFireBase interfaceFireBase;

    public AdapterIndex(Context context, Manager manager, List<Book> books, OnItemClickListener listener) {
        this.manager = manager;
        this.books = books;
        this.listener = listener;
        this.context = context;
        interfaceFireBase = methodsInterface();
    }

    private InterfaceFireBase methodsInterface(){
        return new InterfaceFireBase() {
            @Override
            public void isCorrectlyLogUp(boolean isSuccessful, String error) {

            }

            @Override
            public Book getBook(Book book) {
                return null;
            }

            @Override
            public Author getAuthor(Author author) {
                return null;
            }

            @Override
            public void getUserLogin(FirebaseUser user, String error) {

            }

            @Override
            public List<Book> getAllBooks(List<Book> books) {
                return null;
            }

            @Override
            public List<Author> getAllAuthors(List<Author> authors) {
                return null;
            }

            @Override
            public String sendRoutePhoto(String string) {
                return null;
            }

            @Override
            public String getRoutePhoto(String string) {
                return string;
            }
        };
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflar el layout
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerbook, viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIndex.MyViewHolder myViewHolder, int i) {


        myViewHolder.bind(books.get(i), listener);
    }


    @Override
    public int getItemCount() {
        Log.v("XYZ", "TAMAÑO: " + books.size() + "");
        return books.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{



        TextView item_title, item_author;
        ImageView item_photo, item_start;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.item_title = itemView.findViewById(R.id.item_title);
            this.item_author = itemView.findViewById(R.id.item_author);
            this.item_photo = itemView.findViewById(R.id.item_photo);
            this.item_start = itemView.findViewById(R.id.item_start);
        }

        public void bind(final Book book, final OnItemClickListener listener) {

            /***************************************************************************** *
            * AQUÍ ES DONDE SE LE DAN LOS VALORES A LOS ELEMENTOS DE LA CLASE MYVIEWHOLDER *
            * ******************************************************************************/

            Author author = manager.getAuthor(book.getIdAuthor());

            String nameAuthor = "";
            if(author != null) {
                nameAuthor = author.getName();
            }

            item_title.setText(book.getTitle());
            item_author.setText(nameAuthor);

            FirebaseCustom.getPhoto(book.getUrlPhoto(), interfaceFireBase);

            Picasso.with(context).load(book.getUrlPhoto()).into(item_photo);

            if(book.isFavorite()){
                item_start.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onBookClickListener(book);
                }
            });
        }

    }


}
