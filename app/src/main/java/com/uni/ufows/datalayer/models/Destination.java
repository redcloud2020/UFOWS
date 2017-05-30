package com.uni.ufows.datalayer.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sammy on 3/16/2017.
 */
@Table(name="Destination")
public class Destination extends Model{
    @Column(name="Destination_id")
    String destinationId;
    @Column(name="Destination_ar")
    String destinationAr;


}
