/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.wcrm.core.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.wcrm.core.entity.Record.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Wed Jan 13 10:47:31 CST 2021")
public class Record_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<Record,String> businessids;
    public static volatile SingularAttribute<Record,String> category;
    public static volatile SingularAttribute<Record,String> contactsids;
    public static volatile SingularAttribute<Record,String> content;
    public static volatile SingularAttribute<Record,String> createuser;
    public static volatile SingularAttribute<Record,String> id;
    public static volatile SingularAttribute<Record,Long> longcreatetime;
    public static volatile SingularAttribute<Record,Long> longupdatetime;
    public static volatile SingularAttribute<Record,Date> nexttime;
    public static volatile SingularAttribute<Record,String> types;
    public static volatile SingularAttribute<Record,String> typesid;
}
