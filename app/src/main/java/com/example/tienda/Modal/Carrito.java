package com.example.tienda.Modal;

public class Carrito {
    private String pid, nombre, precio, cantidad, talla, descuento;
    public Carrito(){}

    public Carrito(String pid, String nombre, String precio, String cantidad, String talla, String descuento) {
        this.pid = pid;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.talla = talla;
        this.descuento = descuento;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }
}
