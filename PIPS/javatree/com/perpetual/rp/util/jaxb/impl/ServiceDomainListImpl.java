//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.1-05/30/2003 05:06 AM(java_re)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2003.07.30 at 06:15:01 EDT 
//


package com.perpetual.rp.util.jaxb.impl;

public class ServiceDomainListImpl implements com.perpetual.rp.util.jaxb.ServiceDomainList, com.sun.xml.bind.JAXBObject, com.sun.xml.bind.RIElement, com.perpetual.rp.util.jaxb.impl.runtime.UnmarshallableObject, com.perpetual.rp.util.jaxb.impl.runtime.XMLSerializable, com.perpetual.rp.util.jaxb.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _ServiceDomain = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
    public final static java.lang.Class version = (com.perpetual.rp.util.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "ServiceDomainList";
    }

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (com.perpetual.rp.util.jaxb.ServiceDomainList.class);
    }

    public java.util.List getServiceDomain() {
        return _ServiceDomain;
    }

    public com.perpetual.rp.util.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(com.perpetual.rp.util.jaxb.impl.runtime.UnmarshallingContext context) {
        return new com.perpetual.rp.util.jaxb.impl.ServiceDomainListImpl.Unmarshaller(context);
    }

    public void serializeElementBody(com.perpetual.rp.util.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = _ServiceDomain.size();
        context.startElement("", "ServiceDomainList");
        int idx_0 = idx1;
        while (idx_0 != len1) {
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ServiceDomain.get(idx_0 ++)));
        }
        context.endNamespaceDecls();
        int idx_1 = idx1;
        while (idx_1 != len1) {
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ServiceDomain.get(idx_1 ++)));
        }
        context.endAttributes();
        while (idx1 != len1) {
            context.childAsElementBody(((com.sun.xml.bind.JAXBObject) _ServiceDomain.get(idx1 ++)));
        }
        context.endElement();
    }

    public void serializeAttributes(com.perpetual.rp.util.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = _ServiceDomain.size();
    }

    public void serializeAttributeBody(com.perpetual.rp.util.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = _ServiceDomain.size();
        context.startElement("", "ServiceDomainList");
        int idx_0 = idx1;
        while (idx_0 != len1) {
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ServiceDomain.get(idx_0 ++)));
        }
        context.endNamespaceDecls();
        int idx_1 = idx1;
        while (idx_1 != len1) {
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ServiceDomain.get(idx_1 ++)));
        }
        context.endAttributes();
        while (idx1 != len1) {
            context.childAsElementBody(((com.sun.xml.bind.JAXBObject) _ServiceDomain.get(idx1 ++)));
        }
        context.endElement();
    }

    public void serializeURIs(com.perpetual.rp.util.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = _ServiceDomain.size();
    }

    public java.lang.Class getPrimaryInterface() {
        return (com.perpetual.rp.util.jaxb.ServiceDomainList.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0003I\u0000\u000ecachedHashCodeL\u0000\u0013epsilon"
+"Reducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xp\u0000\u00f7\u00c3Xp"
+"p\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.ms"
+"v.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~\u0000\u0004"
+"\u0000\u00f7\u00c3Mppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom"
+".sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004\u0000\u00f7\u00c3Bsr\u0000"
+"\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0000\u0000\u00f7\u00c3?q\u0000~\u0000\u000ep\u0000sq"
+"\u0000~\u0000\u0007\u0000\u00f7\u00c34ppsq\u0000~\u0000\n\u0000\u00f7\u00c3)q\u0000~\u0000\u000epsr\u0000 com.sun.msv.grammar.AttributeE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004\u0000\u00f7\u00c3&q\u0000~\u0000\u000epsr\u0000"
+"2com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xq\u0000~\u0000\u0004\u0000\u0000\u0000\bsq\u0000~\u0000\r\u0001q\u0000~\u0000\u0015sr\u0000 com.sun.msv.grammar.AnyNameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000"
+"0com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000\u0004\u0000\u0000\u0000\tq\u0000~\u0000\u0016psr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001dx"
+"q\u0000~\u0000\u0018t\u0000(com.perpetual.rp.util.jaxb.ServiceDomaint\u0000+http://ja"
+"va.sun.com/jaxb/xjc/dummy-elementsq\u0000~\u0000\u001bsq\u0000~\u0000\u001ct\u0000\u0011ServiceDomai"
+"nListt\u0000\u0000sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\b"
+"expTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xp"
+"sr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0002\u0000\u0004"
+"I\u0000\u0005countI\u0000\tthresholdL\u0000\u0006parentq\u0000~\u0000%[\u0000\u0005tablet\u0000![Lcom/sun/msv/g"
+"rammar/Expression;xp\u0000\u0000\u0000\u0004\u0000\u0000\u00009pur\u0000![Lcom.sun.msv.grammar.Expre"
+"ssion;\u00d68D\u00c3]\u00ad\u00a7\n\u0002\u0000\u0000xp\u0000\u0000\u0000\u00bfppppppppppppppppppppppppppppppppppppp"
+"ppppppppppppppppppppppppq\u0000~\u0000\u0011ppppppppppq\u0000~\u0000\u0010pppppppppppppq\u0000~"
+"\u0000\fppppppppppq\u0000~\u0000\tppppppppppppppppppppppppppppppppppppppppppp"
+"pppppppppppppppppppppppppppppppppppppppppppppppppp"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends com.perpetual.rp.util.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(com.perpetual.rp.util.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(com.perpetual.rp.util.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return com.perpetual.rp.util.jaxb.impl.ServiceDomainListImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("ServiceDomainList" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  2 :
                        if (("ServiceDomain" == ___local)&&("" == ___uri)) {
                            _ServiceDomain.add(((com.perpetual.rp.util.jaxb.impl.ServiceDomainImpl) spawnChildFromEnterElement((com.perpetual.rp.util.jaxb.impl.ServiceDomainImpl.class), 2, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  1 :
                        if (("ServiceDomain" == ___local)&&("" == ___uri)) {
                            _ServiceDomain.add(((com.perpetual.rp.util.jaxb.impl.ServiceDomainImpl) spawnChildFromEnterElement((com.perpetual.rp.util.jaxb.impl.ServiceDomainImpl.class), 2, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        state = 2;
                        continue outer;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("ServiceDomainList" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        state = 2;
                        continue outer;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        state = 2;
                        continue outer;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        state = 2;
                        continue outer;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  3 :
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            state = 2;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}