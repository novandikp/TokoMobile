package com.itbrain.aplikasitoko;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Adapter;
        import android.widget.ImageView;
        import android.widget.PopupMenu;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.itbrain.aplikasitoko.Model.Kategori;
        import com.itbrain.aplikasitoko.Model.Pegawai;

        import java.util.ArrayList;

public class MenuDaftarPegawaiLaundry extends AppCompatActivity {
    ArrayList<Pegawai> datapegawai;
    RecyclerView recyclerView;
    PegawaiLaundryAdapter adapter;
    DatabaseLaundry db;
//    implements PopupMenu.OnMenuItemClickListener

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_daftar_pegawai_laundry);
        recyclerView = findViewById(R.id.daftarPegawai);
        datapegawai = new ArrayList<>();
        db = new DatabaseLaundry(this);
        adapter = new PegawaiLaundryAdapter(datapegawai,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    public void UbahPegawai(View view) {
        Intent intent = new Intent(MenuDaftarPegawaiLaundry.this, MenuPegawaiLaundry.class);
        startActivity(intent);
    }

//    public void popMenu(View v){
//        datapegawai.remove(recyclerView.getAdapter().toString());
//        adapter = new KategoriLaundryAdapter(datapegawai,this);
//        PopupMenu popupMenu = new PopupMenu(this, v);
//        PopupMenu.OnMenuItemClickListener popMenu;
//        popupMenu.inflate(R.menu.option_item);
//        popupMenu.show();
//    }


//    public boolean onMenuItemClick(MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.listItem:
//                Toast.makeText(this, "Item 1 di Klik", Toast.LENGTH_SHORT).show();
//            case R.id.listItem2:
//                Toast.makeText(this, "Item 2 di Klik", Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

//    public void dummy(){
//        datapegawai.clear();
//        datapegawai.add(new Pegawai(1,"Toyek","Mojokerto","12345"));
//        datapegawai.add(new Pegawai(1,"Toyek","Mojokerto","12345"));
//        datapegawai.add(new Pegawai(1,"Toyek","Mojokerto","12345"));
//    }


    public void getData(){
        Cursor cursor = db.sq("select * from tblpegawai where idpegawai!=0 ");
        if(cursor!=null){
            datapegawai.clear();
            while(cursor.moveToNext()){
                datapegawai.add(new Pegawai(cursor.getInt(cursor.getColumnIndex("idpegawai")),cursor.getString(cursor.getColumnIndex("pegawai")),cursor.getString(cursor.getColumnIndex("alamatpegawai")),cursor.getString(cursor.getColumnIndex("notelppegawai"))));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void Simpan(View view) {
        Intent intent = new Intent(MenuDaftarPegawaiLaundry.this, MenuPegawaiLaundry.class);
        startActivity(intent);
    }
}

class PegawaiLaundryAdapter extends RecyclerView.Adapter<PegawaiLaundryAdapter.ViewHolder>{

    ArrayList<Pegawai>Pegawai;
    Context context;

    public PegawaiLaundryAdapter(ArrayList<Pegawai> Pegawai, Context context) {
        this.Pegawai = Pegawai;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.laundryitemdaftarpegawai,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int Position) {
        final Pegawai pegawai = Pegawai.get(Position);
        holder.namaPegawai.setText(pegawai.getPegawai());
        holder.alamatPegawai.setText(pegawai.getAlamatpegawai());
        holder.noTelpPegawai.setText(pegawai.getNotelppegawai());
        holder.optMuncul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.inflate(R.menu.option_item);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.ubah:
                                Intent intent = new Intent(context,MenuPegawaiLaundry.class);
                                intent.putExtra("idpegawai",pegawai.getIdpegawai());
                                intent.putExtra("pegawai",pegawai.getPegawai());
                                intent.putExtra("alamatpegawai",pegawai.getAlamatpegawai());
                                intent.putExtra("notelppegawai",pegawai.getNotelppegawai());
                                context.startActivity(intent);
                                break;
                            case R.id.hapus:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LaundryDatabase db = new LaundryDatabase(context);
                                        if (db.deletePegawai(pegawai.getIdpegawai())){
                                            Pegawai.remove(Position);
                                            notifyItemChanged(Position);
                                            Toast.makeText(context,pegawai.getPegawai()+" "+"berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setTitle("Hapus "+pegawai.getPegawai());
                                builder.setMessage("Anda yakin ingin menghapus "+pegawai.getPegawai()+" dari data Pegawai");
                                builder.show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Pegawai.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaPegawai,alamatPegawai,noTelpPegawai;
        ImageView optMuncul;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaPegawai = itemView.findViewById(R.id.namaPegawai);
            alamatPegawai = itemView.findViewById(R.id.alamatPegawai);
            noTelpPegawai = itemView.findViewById(R.id.notelpPegawai);
            optMuncul = itemView.findViewById(R.id.optMuncul);
        }
    }
}