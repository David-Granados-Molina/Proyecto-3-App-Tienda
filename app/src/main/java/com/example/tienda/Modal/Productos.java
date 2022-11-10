package com.example.tienda.Modal;

import android.widget.Spinner;

public class Productos {

    private String nombre, descripcion, precio, categoria, pid, cantidad, fecha, hora, imagen;
    private String talla;

    public Productos(){}

    public Productos(String nombre, String descripcion, String talla, String precio, String categoria, String pid, String cantidad, String fecha, String hora, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.talla = talla;
        this.precio = precio;
        this.categoria = categoria;
        this.pid = pid;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.hora = hora;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
