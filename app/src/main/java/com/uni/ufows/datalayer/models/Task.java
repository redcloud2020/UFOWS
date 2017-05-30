package com.uni.ufows.datalayer.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sammy on 3/16/2017.
 */
@Table(name="Task")
public class Task extends Model{
    @Column(name="task_id", unique = true)
    String taskId;
    @Column(name="task_type")
    String taskType;
    @Column(name="task_priority")
    String taskPriority;
    @Column(name="task_order")
    String taskOrder;
    @Column(name="batch_number")
    String batchNumber;
    @Column(name="task_status")
    String taskStatus;
    @Column(name="timestamp_done")
    String timeStampDone;
    @Column(name="tank_id")
    String tankId;
    @Column(name="truck_id")
    String truckId;
    @Column(name="estimated_volume")
    String estimatedVolume;
}
