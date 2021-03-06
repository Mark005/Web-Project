package com.nncompany.api.model.entities;

import com.nncompany.api.model.enums.Gender;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@Indexed
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "certificate_number")
    private Integer certificateNumber;

    @Field(termVector = TermVector.YES)
    @Analyzer(impl = KeywordAnalyzer.class)
    private String name;

    @Field(termVector = TermVector.YES)
    @Analyzer(impl = KeywordAnalyzer.class)
    private String surname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profession;

    @Column(name = "date_employment")
    private Date dateEmployment;

    private Boolean isAdmin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private UserCreds userCreds;

    @Override
    public boolean equals(Object obj) {
        User m = (User) obj;
        return this.id.equals(m.getId());
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(Integer certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Date getDateEmployment() {
        return dateEmployment;
    }

    public void setDateEmployment(Date dateEmployment) {
        this.dateEmployment = dateEmployment;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
