/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.wcrm.core.entity;

import com.x.base.core.entity.StorageObject_;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.wcrm.core.entity.Attachment.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Wed Jan 13 10:47:31 CST 2021")
public class Attachment_ extends StorageObject_  {
    public static volatile ListAttribute<Attachment,String> controllerIdentityList;
    public static volatile ListAttribute<Attachment,String> controllerUnitList;
    public static volatile SingularAttribute<Attachment,String> crmType;
    public static volatile SingularAttribute<Attachment,Boolean> deepPath;
    public static volatile ListAttribute<Attachment,String> editIdentityList;
    public static volatile ListAttribute<Attachment,String> editUnitList;
    public static volatile SingularAttribute<Attachment,String> extension;
    public static volatile SingularAttribute<Attachment,String> id;
    public static volatile SingularAttribute<Attachment,String> lastUpdatePerson;
    public static volatile SingularAttribute<Attachment,Date> lastUpdateTime;
    public static volatile SingularAttribute<Attachment,Long> length;
    public static volatile SingularAttribute<Attachment,String> name;
    public static volatile SingularAttribute<Attachment,String> preview;
    public static volatile ListAttribute<Attachment,String> readIdentityList;
    public static volatile ListAttribute<Attachment,String> readUnitList;
    public static volatile SingularAttribute<Attachment,String> site;
    public static volatile SingularAttribute<Attachment,String> storage;
    public static volatile SingularAttribute<Attachment,String> text;
    public static volatile SingularAttribute<Attachment,String> type;
    public static volatile SingularAttribute<Attachment,String> wcrm;
    public static volatile SingularAttribute<Attachment,Date> workCreateTime;
}
