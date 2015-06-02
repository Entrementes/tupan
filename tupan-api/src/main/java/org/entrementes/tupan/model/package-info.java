@XmlSchema(
    namespace = "http://www.entrementes.org/tupan/1.0",
    elementFormDefault = XmlNsForm.QUALIFIED,
    xmlns = {
        @XmlNs(prefix="tupan", namespaceURI="http://www.entrementes.org/tupan/1.0")
    }
) 
package org.entrementes.tupan.model;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;