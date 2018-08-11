package com.example.greendao;


import javax.xml.validation.Validator;
import javax.xml.validation.ValidatorHandler;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String[] args) {

        Schema schema = new Schema(1000,"com.example.msp.greendao.entity.greendao");
        addNote(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addNote(Schema schema) {
        Entity entity = schema.addEntity("WisdomEntity");
        entity.addIdProperty().autoincrement().primaryKey();
        entity.addStringProperty("english");
        entity.addStringProperty("china");
        Entity entity1 = schema.addEntity("CET4Entity");
        entity1.addStringProperty("word");
        entity1.addStringProperty("english");
        entity1.addStringProperty("china");
        entity1.addStringProperty("sign");

    }

}
