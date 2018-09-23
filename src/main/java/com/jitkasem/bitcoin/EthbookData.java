package com.jitkasem.bitcoin;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ethbook_data")
public class EthbookData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ethPri")
    private Float ethPri;

    @Column(name = "timeRecord")
    private String timeRecord;

}
