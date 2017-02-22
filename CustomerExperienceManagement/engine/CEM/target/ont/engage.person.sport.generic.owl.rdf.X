<?xml version="1.0"?>


<!DOCTYPE rdf:RDF [
    <!ENTITY j.0 "http://engage/ontology/base#" >
    <!ENTITY place "http://engage/ontology/place#" >
    <!ENTITY owl "http://www.w3.org/2002/07/owl#" >
    <!ENTITY product "http://engage/ontology/product#" >
    <!ENTITY xsd "http://www.w3.org/2001/XMLSchema#" >
    <!ENTITY person "http://engage/ontology/person360#" >
    <!ENTITY rdfs "http://www.w3.org/2000/01/rdf-schema#" >
    <!ENTITY ace_lexicon "http://attempto.ifi.uzh.ch/ace_lexicon#" >
    <!ENTITY rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#" >
]>


<rdf:RDF xmlns="http://engage/ontology/person360#"
     xml:base="http://engage/ontology/person360"
     xmlns:product="http://engage/ontology/product#"
     xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
     xmlns:person="http://engage/ontology/person360#"
     xmlns:owl="http://www.w3.org/2002/07/owl#"
     xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
     xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
     xmlns:j.0="http://engage/ontology/base#"
     xmlns:place="http://engage/ontology/place#"
     xmlns:ace_lexicon="http://attempto.ifi.uzh.ch/ace_lexicon#">
    <owl:Ontology rdf:about="http://engage/ontology/person360">
        <rdfs:label xml:lang="en">BaseOntology</rdfs:label>
        <rdfs:label xml:lang="en">PersonOtology</rdfs:label>
        <rdfs:label xml:lang="en">PlaceOntology</rdfs:label>
        <rdfs:label xml:lang="en">Product Otology</rdfs:label>
        <rdfs:comment xml:lang="en">BaseOntology</rdfs:comment>
        <rdfs:comment xml:lang="en">Consumable Product Ontology</rdfs:comment>
        <rdfs:comment xml:lang="en">Person Ontology</rdfs:comment>
        <rdfs:comment xml:lang="en">Place Ontology</rdfs:comment>
    </owl:Ontology>
    


    <!-- 
    ///////////////////////////////////////////////////////////////////////////////////////
    //
    // Annotation properties
    //
    ///////////////////////////////////////////////////////////////////////////////////////
     -->

    


    <!-- http://attempto.ifi.uzh.ch/ace_lexicon#CN_pl -->

    <owl:AnnotationProperty rdf:about="&ace_lexicon;CN_pl"/>
    


    <!-- http://attempto.ifi.uzh.ch/ace_lexicon#CN_sg -->

    <owl:AnnotationProperty rdf:about="&ace_lexicon;CN_sg"/>
    


    <!-- http://attempto.ifi.uzh.ch/ace_lexicon#PN_sg -->

    <owl:AnnotationProperty rdf:about="&ace_lexicon;PN_sg"/>
    


    <!-- http://attempto.ifi.uzh.ch/ace_lexicon#TV_pl -->

    <owl:AnnotationProperty rdf:about="&ace_lexicon;TV_pl"/>
    


    <!-- http://attempto.ifi.uzh.ch/ace_lexicon#TV_sg -->

    <owl:AnnotationProperty rdf:about="&ace_lexicon;TV_sg"/>
    


    <!-- http://attempto.ifi.uzh.ch/ace_lexicon#TV_vbg -->

    <owl:AnnotationProperty rdf:about="&ace_lexicon;TV_vbg"/>
    


    <!-- 
    ///////////////////////////////////////////////////////////////////////////////////////
    //
    // Datatypes
    //
    ///////////////////////////////////////////////////////////////////////////////////////
     -->

    


    <!-- http://www.w3.org/2001/XMLSchema#date -->

    <rdfs:Datatype rdf:about="&xsd;date"/>
    


    <!-- 
    ///////////////////////////////////////////////////////////////////////////////////////
    //
    // Object Properties
    //
    ///////////////////////////////////////////////////////////////////////////////////////
     -->

    


    <!-- http://engage/ontology/base#BaseProperty -->

    <rdf:Description rdf:about="&j.0;BaseProperty">
        <ace_lexicon:TV_vbg>BasePropertied</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>BaseProperty</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>BaseProperties</ace_lexicon:TV_sg>
    </rdf:Description>
    


    <!-- http://engage/ontology/person360#Coleague -->

    <owl:ObjectProperty rdf:about="&person;Coleague">
        <ace_lexicon:TV_vbg>Coleagued</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>Coleague</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>Coleague</ace_lexicon:PN_sg>
        <ace_lexicon:TV_sg>Coleagues</ace_lexicon:TV_sg>
        <rdfs:subPropertyOf rdf:resource="&person;WorkRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#FamilyRelationship -->

    <owl:ObjectProperty rdf:about="&person;FamilyRelationship">
        <ace_lexicon:TV_pl>FamilyRelationship</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>FamilyRelationshiped</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>FamilyRelationships</ace_lexicon:TV_sg>
        <ace_lexicon:CN_pl>FamilyRelationships</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>FamilyRelationship</ace_lexicon:CN_sg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#Has -->

    <owl:ObjectProperty rdf:about="&person;Has">
        <ace_lexicon:TV_vbg>Hased</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>Hases</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>Has</ace_lexicon:TV_pl>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#Likes -->

    <owl:ObjectProperty rdf:about="&person;Likes">
        <ace_lexicon:TV_vbg>Likesed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>Likes</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>Likes</ace_lexicon:PN_sg>
        <ace_lexicon:TV_sg>Likeses</ace_lexicon:TV_sg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#LivesIn -->

    <owl:ObjectProperty rdf:about="&person;LivesIn">
        <ace_lexicon:TV_sg>LivesIns</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>LivesIned</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>LivesIn</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>LivesIn</ace_lexicon:PN_sg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#Manager -->

    <owl:ObjectProperty rdf:about="&person;Manager">
        <ace_lexicon:TV_sg>Managers</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>Manager</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>Managered</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;WorkRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#Owns -->

    <owl:ObjectProperty rdf:about="&person;Owns">
        <ace_lexicon:TV_sg>Ownses</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>Ownsed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>Owns</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>Owns</ace_lexicon:PN_sg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#Peer -->

    <owl:ObjectProperty rdf:about="&person;Peer">
        <ace_lexicon:TV_vbg>Peered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>Peers</ace_lexicon:TV_sg>
        <ace_lexicon:PN_sg>Peer</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>Peer</ace_lexicon:TV_pl>
        <rdfs:subPropertyOf rdf:resource="&person;WorkRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#ReportsTo -->

    <owl:ObjectProperty rdf:about="&person;ReportsTo">
        <ace_lexicon:TV_pl>ReportsTo</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>ReportsToes</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>ReportsToed</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;WorkRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#WorkRelationship -->

    <owl:ObjectProperty rdf:about="&person;WorkRelationship">
        <ace_lexicon:TV_sg>WorkRelationships</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>WorkRelationshiped</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>WorkRelationship</ace_lexicon:TV_pl>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#WorksAt -->

    <owl:ObjectProperty rdf:about="&person;WorksAt">
        <ace_lexicon:TV_sg>WorksAts</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>WorksAt</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>WorksAt</ace_lexicon:PN_sg>
        <ace_lexicon:TV_vbg>WorksAted</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
        <rdfs:domain rdf:resource="&person;Person"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasAlergy -->

    <owl:ObjectProperty rdf:about="&person;hasAlergy">
        <ace_lexicon:TV_sg>hasAlergies</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasAlergy</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasAlergied</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasAquaintance -->

    <owl:ObjectProperty rdf:about="&person;hasAquaintance">
        <ace_lexicon:TV_sg>hasAquaintances</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>hasAquaintanced</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>hasAquaintance</ace_lexicon:TV_pl>
        <rdfs:subPropertyOf rdf:resource="&person;FamilyRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasAunt -->

    <owl:ObjectProperty rdf:about="&person;hasAunt">
        <ace_lexicon:TV_sg>hasAunts</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>hasAunted</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>hasAunt</ace_lexicon:TV_pl>
        <rdfs:subPropertyOf rdf:resource="&person;hasRelative"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasBrother -->

    <owl:ObjectProperty rdf:about="&person;hasBrother">
        <ace_lexicon:TV_vbg>hasBrothered</ace_lexicon:TV_vbg>
        <ace_lexicon:PN_sg>hasBrother</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasBrother</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>hasBrothers</ace_lexicon:TV_sg>
        <rdfs:subPropertyOf rdf:resource="&person;hasSibling"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasChild -->

    <owl:ObjectProperty rdf:about="&person;hasChild">
        <ace_lexicon:TV_sg>hasChilds</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasChild</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>hasChild</ace_lexicon:PN_sg>
        <ace_lexicon:TV_vbg>hasChilded</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;FamilyRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasCousin -->

    <owl:ObjectProperty rdf:about="&person;hasCousin">
        <ace_lexicon:TV_sg>hasCousins</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasCousin</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasCousined</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;hasRelative"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasDaughter -->

    <owl:ObjectProperty rdf:about="&person;hasDaughter">
        <ace_lexicon:PN_sg>hasDaughter</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasDaughter</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasDaughtered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>hasDaughters</ace_lexicon:TV_sg>
        <rdfs:subPropertyOf rdf:resource="&person;hasChild"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasDoctor -->

    <owl:ObjectProperty rdf:about="&person;hasDoctor">
        <ace_lexicon:TV_sg>hasDoctors</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasDoctor</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasDoctored</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasFather -->

    <owl:ObjectProperty rdf:about="&person;hasFather">
        <ace_lexicon:PN_sg>hasFather</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasFather</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>hasFathers</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>hasFathered</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;hasParent"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasFriend -->

    <owl:ObjectProperty rdf:about="&person;hasFriend">
        <ace_lexicon:TV_pl>hasFriend</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasFriended</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>hasFriends</ace_lexicon:TV_sg>
        <rdfs:subPropertyOf rdf:resource="&person;hasAquaintance"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasIllness -->

    <owl:ObjectProperty rdf:about="&person;hasIllness">
        <ace_lexicon:TV_pl>hasIllness</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>hasIllnesses</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>hasIllnessed</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasMother -->

    <owl:ObjectProperty rdf:about="&person;hasMother">
        <ace_lexicon:TV_sg>hasMothers</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>hasMothered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>hasMother</ace_lexicon:TV_pl>
        <rdfs:subPropertyOf rdf:resource="&person;hasParent"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasParent -->

    <owl:ObjectProperty rdf:about="&person;hasParent">
        <ace_lexicon:TV_pl>hasParent</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasParented</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>hasParents</ace_lexicon:TV_sg>
        <rdfs:subPropertyOf rdf:resource="&person;FamilyRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasRelative -->

    <owl:ObjectProperty rdf:about="&person;hasRelative">
        <ace_lexicon:TV_sg>hasRelatives</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasRelative</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasRelatived</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;FamilyRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasSibling -->

    <owl:ObjectProperty rdf:about="&person;hasSibling">
        <rdf:type rdf:resource="&owl;SymmetricProperty"/>
        <ace_lexicon:TV_sg>hasSiblings</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasSibling</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasSiblinged</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;FamilyRelationship"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasSister -->

    <owl:ObjectProperty rdf:about="&person;hasSister">
        <ace_lexicon:TV_vbg>hasSistered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>hasSisters</ace_lexicon:TV_sg>
        <ace_lexicon:PN_sg>hasSister</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasSister</ace_lexicon:TV_pl>
        <rdfs:subPropertyOf rdf:resource="&person;hasSibling"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasSon -->

    <owl:ObjectProperty rdf:about="&person;hasSon">
        <ace_lexicon:TV_sg>hasSons</ace_lexicon:TV_sg>
        <ace_lexicon:PN_sg>hasSon</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasSon</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasSoned</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;hasChild"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/person360#hasUncle -->

    <owl:ObjectProperty rdf:about="&person;hasUncle">
        <ace_lexicon:TV_sg>hasUncles</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasUncle</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasUncled</ace_lexicon:TV_vbg>
        <rdfs:subPropertyOf rdf:resource="&person;hasRelative"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/place#hasPlace -->

    <owl:ObjectProperty rdf:about="&place;hasPlace">
        <ace_lexicon:TV_vbg>hasPlaced</ace_lexicon:TV_vbg>
        <ace_lexicon:PN_sg>hasPlace</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasPlace</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>hasPlaces</ace_lexicon:TV_sg>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- http://engage/ontology/place#placeBelongsTo -->

    <owl:ObjectProperty rdf:about="&place;placeBelongsTo">
        <ace_lexicon:TV_sg>placeBelongsToes</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>placeBelongsToed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>placeBelongsTo</ace_lexicon:TV_pl>
        <rdfs:subPropertyOf rdf:resource="&j.0;BaseProperty"/>
    </owl:ObjectProperty>
    


    <!-- 
    ///////////////////////////////////////////////////////////////////////////////////////
    //
    // Data properties
    //
    ///////////////////////////////////////////////////////////////////////////////////////
     -->

    


    <!-- http://engage/ontology/base#ClassCounter -->

    <owl:DatatypeProperty rdf:about="&j.0;ClassCounter">
        <ace_lexicon:TV_pl>ClassCounter</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>ClassCounters</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>ClassCountered</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&j.0;BaseClass"/>
        <rdfs:range rdf:resource="&xsd;long"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/base#PropertyCounter -->

    <owl:DatatypeProperty rdf:about="&j.0;PropertyCounter">
        <ace_lexicon:TV_sg>PropertyCounters</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>PropertyCountered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>PropertyCounter</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&j.0;BaseClass"/>
        <rdfs:range rdf:resource="&xsd;long"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#Address -->

    <owl:DatatypeProperty rdf:about="&person;Address">
        <ace_lexicon:TV_pl>Address</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>Addresses</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>Addressed</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#Age -->

    <owl:DatatypeProperty rdf:about="&person;Age">
        <ace_lexicon:TV_vbg>Aged</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>Ages</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>Age</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;integer"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#BandName -->

    <owl:DatatypeProperty rdf:about="&person;BandName">
        <ace_lexicon:TV_sg>BandNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>BandName</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>BandNamed</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;Band"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#BandStyle -->

    <owl:DatatypeProperty rdf:about="&person;BandStyle">
        <ace_lexicon:TV_pl>BandStyle</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>BandStyled</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>BandStyles</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;Band"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#BrowsingProcessed -->

    <owl:DatatypeProperty rdf:about="&person;BrowsingProcessed">
        <ace_lexicon:TV_vbg>BrowsingProcesseded</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>BrowsingProcesseds</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>BrowsingProcessed</ace_lexicon:TV_pl>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#CardNumber -->

    <owl:DatatypeProperty rdf:about="&person;CardNumber">
        <ace_lexicon:TV_sg>CardNumbers</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>CardNumber</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>CardNumbered</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;PaymentPreference"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#CardType -->

    <owl:DatatypeProperty rdf:about="&person;CardType">
        <ace_lexicon:TV_sg>CardTypes</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>CardType</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>CardTyped</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;PaymentPreference"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#CompanyAddress -->

    <owl:DatatypeProperty rdf:about="&person;CompanyAddress">
        <ace_lexicon:TV_vbg>CompanyAddressed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>CompanyAddress</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>CompanyAddresses</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;WorkPlace"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#CompanyName -->

    <owl:DatatypeProperty rdf:about="&person;CompanyName">
        <ace_lexicon:TV_sg>CompanyNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>CompanyName</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>CompanyNamed</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;WorkPlace"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#DateOfBirth -->

    <owl:DatatypeProperty rdf:about="&person;DateOfBirth">
        <ace_lexicon:TV_sg>DateOfBirths</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>DateOfBirthed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>DateOfBirth</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#Degree -->

    <owl:DatatypeProperty rdf:about="&person;Degree">
        <ace_lexicon:TV_vbg>Degreed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>Degrees</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>Degree</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;FamilyRelationship"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#FaceBookId -->

    <owl:DatatypeProperty rdf:about="&person;FaceBookId">
        <ace_lexicon:TV_sg>FaceBookIds</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>FaceBookId</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>FaceBookIded</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#FirstName -->

    <owl:DatatypeProperty rdf:about="&person;FirstName">
        <ace_lexicon:TV_sg>FirstNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>FirstName</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>FirstNamed</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#GameSentiment -->

    <owl:DatatypeProperty rdf:about="&person;GameSentiment">
        <ace_lexicon:TV_vbg>GameSentimented</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>GameSentiments</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>GameSentiment</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;Sport"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#Gender -->

    <owl:DatatypeProperty rdf:about="&person;Gender">
        <ace_lexicon:TV_pl>Gender</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>Genders</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>Gendered</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#ID -->

    <owl:DatatypeProperty rdf:about="&person;ID">
        <ace_lexicon:TV_vbg>IDed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>IDs</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>ID</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#IssuingBank -->

    <owl:DatatypeProperty rdf:about="&person;IssuingBank">
        <ace_lexicon:TV_pl>IssuingBank</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>IssuingBanked</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>IssuingBanks</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;PaymentPreference"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#LastName -->

    <owl:DatatypeProperty rdf:about="&person;LastName">
        <ace_lexicon:TV_sg>LastNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>LastName</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>LastNamed</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#LinkedInId -->

    <owl:DatatypeProperty rdf:about="&person;LinkedInId">
        <ace_lexicon:TV_vbg>LinkedInIded</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>LinkedInIds</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>LinkedInId</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#LoyaltyTier -->

    <owl:DatatypeProperty rdf:about="&person;LoyaltyTier">
        <ace_lexicon:TV_sg>LoyaltyTiers</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>LoyaltyTiered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>LoyaltyTier</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;LoyaltyProgram"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#LoyaltyUserId -->

    <owl:DatatypeProperty rdf:about="&person;LoyaltyUserId">
        <ace_lexicon:TV_sg>LoyaltyUserIds</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>LoyaltyUserIded</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>LoyaltyUserId</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;LoyaltyProgram"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#MusicStyle -->

    <owl:DatatypeProperty rdf:about="&person;MusicStyle">
        <ace_lexicon:TV_vbg>MusicStyled</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>MusicStyle</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>MusicStyles</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;Musician"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#MusicianName -->

    <owl:DatatypeProperty rdf:about="&person;MusicianName">
        <ace_lexicon:TV_pl>MusicianName</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>MusicianNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>MusicianNamed</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;Musician"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#Name -->

    <owl:DatatypeProperty rdf:about="&person;Name">
        <ace_lexicon:TV_sg>Names</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>Named</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>Name</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#NumGamesAttendedSeason -->

    <owl:DatatypeProperty rdf:about="&person;NumGamesAttendedSeason">
        <ace_lexicon:TV_vbg>NumGamesAttendedSeasoned</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>NumGamesAttendedSeason</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>NumGamesAttendedSeasons</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;Sport"/>
        <rdfs:range rdf:resource="&xsd;integer"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#NumGamesAttendedTotal -->

    <owl:DatatypeProperty rdf:about="&person;NumGamesAttendedTotal">
        <ace_lexicon:TV_vbg>NumGamesAttendedTotaled</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>NumGamesAttendedTotals</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>NumGamesAttendedTotal</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;Sport"/>
        <rdfs:range rdf:resource="&xsd;integer"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#PlayerName -->

    <owl:DatatypeProperty rdf:about="&person;PlayerName">
        <ace_lexicon:TV_vbg>PlayerNamed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>PlayerNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>PlayerName</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;SportsPlayer"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#Profession -->

    <owl:DatatypeProperty rdf:about="&person;Profession">
        <ace_lexicon:TV_vbg>Professioned</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>Profession</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>Professions</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#SocialMediaFriendApp -->

    <owl:DatatypeProperty rdf:about="&person;SocialMediaFriendApp">
        <ace_lexicon:TV_vbg>SocialMediaFriendApped</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>SocialMediaFriendApps</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>SocialMediaFriendApp</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;SocialMediaFriend"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#SocialMediaFriendName -->

    <owl:DatatypeProperty rdf:about="&person;SocialMediaFriendName">
        <ace_lexicon:TV_sg>SocialMediaFriendNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>SocialMediaFriendNamed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>SocialMediaFriendName</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;SocialMediaFriend"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#SportType -->

    <owl:DatatypeProperty rdf:about="&person;SportType">
        <ace_lexicon:TV_vbg>SportTyped</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>SportTypes</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>SportType</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;Sport"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#TeamName -->

    <owl:DatatypeProperty rdf:about="&person;TeamName">
        <ace_lexicon:TV_vbg>TeamNamed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>TeamName</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>TeamNames</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;SportsTeam"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#TotalLoyaltyPoints -->

    <owl:DatatypeProperty rdf:about="&person;TotalLoyaltyPoints">
        <ace_lexicon:TV_sg>TotalLoyaltyPointses</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>TotalLoyaltyPointsed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>TotalLoyaltyPoints</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;LoyaltyProgram"/>
        <rdfs:range rdf:resource="&xsd;integer"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#TwitterId -->

    <owl:DatatypeProperty rdf:about="&person;TwitterId">
        <ace_lexicon:TV_vbg>TwitterIded</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>TwitterId</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>TwitterIds</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#hasEducation -->

    <owl:DatatypeProperty rdf:about="&person;hasEducation">
        <ace_lexicon:TV_sg>hasEducations</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasEducation</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasEducationed</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#speaksLanguage -->

    <owl:DatatypeProperty rdf:about="&person;speaksLanguage">
        <ace_lexicon:TV_pl>speaksLanguage</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>speaksLanguaged</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>speaksLanguages</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&person;Person"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/person360#workTitle -->

    <owl:DatatypeProperty rdf:about="&person;workTitle">
        <ace_lexicon:TV_sg>workTitles</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>workTitled</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>workTitle</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&person;BrowsingPerson"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/place#placeName -->

    <owl:DatatypeProperty rdf:about="&place;placeName">
        <ace_lexicon:TV_sg>placeNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>placeName</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>placeNamed</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&place;PlaceType"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/place#placeType -->

    <owl:DatatypeProperty rdf:about="&place;placeType">
        <ace_lexicon:TV_pl>placeType</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>placeTypes</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>placeTyped</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&place;PlaceType"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ItemGender -->

    <owl:DatatypeProperty rdf:about="&product;ItemGender">
        <ace_lexicon:TV_vbg>ItemGendered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>ItemGenders</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>ItemGender</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&product;Clothing"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ItemSize -->

    <owl:DatatypeProperty rdf:about="&product;ItemSize">
        <ace_lexicon:TV_sg>ItemSizes</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>ItemSize</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>ItemSized</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&product;Clothing"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ProductBrand -->

    <owl:DatatypeProperty rdf:about="&product;ProductBrand">
        <ace_lexicon:TV_vbg>ProductBranded</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>ProductBrands</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>ProductBrand</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&product;Product"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ProductDiscount -->

    <owl:DatatypeProperty rdf:about="&product;ProductDiscount">
        <ace_lexicon:TV_vbg>ProductDiscounted</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>ProductDiscounts</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>ProductDiscount</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&product;Product"/>
        <rdfs:range rdf:resource="&xsd;int"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ProductExpiryDate -->

    <owl:DatatypeProperty rdf:about="&product;ProductExpiryDate">
        <ace_lexicon:TV_pl>ProductExpiryDate</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>ProductExpiryDates</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>ProductExpiryDated</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&product;Product"/>
        <rdfs:range rdf:resource="&xsd;date"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ProductMadeIn -->

    <owl:DatatypeProperty rdf:about="&product;ProductMadeIn">
        <ace_lexicon:TV_vbg>ProductMadeIned</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>ProductMadeIns</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>ProductMadeIn</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&product;Product"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ProductName -->

    <owl:DatatypeProperty rdf:about="&product;ProductName">
        <ace_lexicon:TV_vbg>ProductNamed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>ProductNames</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>ProductName</ace_lexicon:TV_pl>
        <rdfs:domain rdf:resource="&product;Product"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ProductPrice -->

    <owl:DatatypeProperty rdf:about="&product;ProductPrice">
        <ace_lexicon:TV_vbg>ProductPriced</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>ProductPrice</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>ProductPrices</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&product;Product"/>
        <rdfs:range rdf:resource="&xsd;double"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ProductSerialNumber -->

    <owl:DatatypeProperty rdf:about="&product;ProductSerialNumber">
        <ace_lexicon:TV_vbg>ProductSerialNumbered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>ProductSerialNumber</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>ProductSerialNumbers</ace_lexicon:TV_sg>
        <rdfs:domain rdf:resource="&product;Product"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- http://engage/ontology/product#ProductType -->

    <owl:DatatypeProperty rdf:about="&product;ProductType">
        <ace_lexicon:TV_sg>ProductTypes</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>ProductType</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>ProductTyped</ace_lexicon:TV_vbg>
        <rdfs:domain rdf:resource="&product;Product"/>
        <rdfs:range rdf:resource="&xsd;string"/>
    </owl:DatatypeProperty>
    


    <!-- 
    ///////////////////////////////////////////////////////////////////////////////////////
    //
    // Classes
    //
    ///////////////////////////////////////////////////////////////////////////////////////
     -->

    


    <!-- http://engage/ontology/base#BaseClass -->

    <owl:Class rdf:about="&j.0;BaseClass">
        <ace_lexicon:CN_pl>BaseClasses</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>BaseClass</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#Band -->

    <owl:Class rdf:about="&person;Band">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_sg>Band</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Bands</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>Band</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#Baseball -->

    <owl:Class rdf:about="&person;Baseball">
        <rdfs:subClassOf rdf:resource="&person;Sport"/>
        <ace_lexicon:CN_pl>Baseballs</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Baseball</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#Basketball -->

    <owl:Class rdf:about="&person;Basketball">
        <rdfs:subClassOf rdf:resource="&person;Sport"/>
        <ace_lexicon:CN_pl>Basketballs</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Basketball</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#BrowsingPerson -->

    <owl:Class rdf:about="&person;BrowsingPerson">
        <rdfs:subClassOf rdf:resource="&person;Person"/>
        <ace_lexicon:CN_pl>BrowsingPersons</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>BrowsingPerson</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#FamilyRelationship -->

    <rdf:Description rdf:about="&person;FamilyRelationship">
        <ace_lexicon:TV_pl>FamilyRelationship</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>FamilyRelationshiped</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>FamilyRelationships</ace_lexicon:TV_sg>
        <ace_lexicon:CN_pl>FamilyRelationships</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>FamilyRelationship</ace_lexicon:CN_sg>
    </rdf:Description>
    


    <!-- http://engage/ontology/person360#Hockey -->

    <owl:Class rdf:about="&person;Hockey">
        <rdfs:subClassOf rdf:resource="&person;Sport"/>
        <ace_lexicon:CN_sg>Hockey</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Hockeys</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#LoyaltyProgram -->

    <owl:Class rdf:about="&person;LoyaltyProgram">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_pl>LoyaltyPrograms</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>LoyaltyProgram</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>LoyaltyProgram</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#MusicType -->

    <owl:Class rdf:about="&person;MusicType">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_sg>MusicType</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>MusicTypes</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>MusicType</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#Musician -->

    <owl:Class rdf:about="&person;Musician">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:PN_sg>Musician</ace_lexicon:PN_sg>
        <ace_lexicon:CN_pl>Musicians</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Musician</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#PaymentPreference -->

    <owl:Class rdf:about="&person;PaymentPreference">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_sg>PaymentPreference</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>PaymentPreference</ace_lexicon:PN_sg>
        <ace_lexicon:CN_pl>PaymentPreferences</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#Person -->

    <owl:Class rdf:about="&person;Person">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_pl>Persons</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Person</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Person</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#Soccer -->

    <owl:Class rdf:about="&person;Soccer">
        <rdfs:subClassOf rdf:resource="&person;Sport"/>
        <ace_lexicon:CN_pl>Soccers</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Soccer</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#SocialMediaFriend -->

    <owl:Class rdf:about="&person;SocialMediaFriend">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_pl>SocialMediaFriends</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>SocialMediaFriend</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>SocialMediaFriend</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#Sport -->

    <owl:Class rdf:about="&person;Sport">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_pl>Sports</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Sport</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Sport</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#SportsPlayer -->

    <owl:Class rdf:about="&person;SportsPlayer">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_pl>SportsPlayers</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>SportsPlayer</ace_lexicon:PN_sg>
        <ace_lexicon:CN_sg>SportsPlayer</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#SportsTeam -->

    <owl:Class rdf:about="&person;SportsTeam">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_sg>SportsTeam</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>SportsTeam</ace_lexicon:PN_sg>
        <ace_lexicon:CN_pl>SportsTeams</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/person360#WorkPlace -->

    <owl:Class rdf:about="&person;WorkPlace">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_pl>WorkPlaces</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>WorkPlace</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>WorkPlace</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/place#Address -->

    <owl:Class rdf:about="&place;Address">
        <rdfs:subClassOf rdf:resource="&place;PlaceType"/>
        <ace_lexicon:CN_pl>Addresses</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Address</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Address</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/place#City -->

    <owl:Class rdf:about="&place;City">
        <rdfs:subClassOf rdf:resource="&place;PlaceType"/>
        <ace_lexicon:CN_sg>City</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Cities</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>City</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/place#Continent -->

    <owl:Class rdf:about="&place;Continent">
        <rdfs:subClassOf rdf:resource="&place;PlaceType"/>
        <ace_lexicon:PN_sg>Continent</ace_lexicon:PN_sg>
        <ace_lexicon:CN_sg>Continent</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Continents</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/place#Country -->

    <owl:Class rdf:about="&place;Country">
        <rdfs:subClassOf rdf:resource="&place;PlaceType"/>
        <ace_lexicon:CN_sg>Country</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Country</ace_lexicon:PN_sg>
        <ace_lexicon:CN_pl>Countries</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/place#PlaceType -->

    <owl:Class rdf:about="&place;PlaceType">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_pl>PlaceTypes</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>PlaceType</ace_lexicon:PN_sg>
        <ace_lexicon:CN_sg>PlaceType</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/place#Planet -->

    <owl:Class rdf:about="&place;Planet">
        <rdfs:subClassOf rdf:resource="&place;PlaceType"/>
        <ace_lexicon:CN_pl>Planets</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Planet</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Planet</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/place#Region -->

    <owl:Class rdf:about="&place;Region">
        <rdfs:subClassOf rdf:resource="&place;PlaceType"/>
        <ace_lexicon:CN_sg>Region</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Regions</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>Region</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/place#State -->

    <owl:Class rdf:about="&place;State">
        <rdfs:subClassOf rdf:resource="&place;PlaceType"/>
        <ace_lexicon:PN_sg>State</ace_lexicon:PN_sg>
        <ace_lexicon:CN_sg>State</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>States</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Alcoholic -->

    <owl:Class rdf:about="&product;Alcoholic">
        <rdfs:subClassOf rdf:resource="&product;Drink"/>
        <ace_lexicon:CN_pl>Alcoholics</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Alcoholic</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Appliance -->

    <owl:Class rdf:about="&product;Appliance">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_pl>Appliances</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Appliance</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Appliance</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#BeachWear -->

    <owl:Class rdf:about="&product;BeachWear">
        <rdfs:subClassOf rdf:resource="&product;Clothing"/>
        <ace_lexicon:CN_sg>BeachWear</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>BeachWears</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Beer -->

    <owl:Class rdf:about="&product;Beer">
        <rdfs:subClassOf rdf:resource="&product;Alcoholic"/>
        <ace_lexicon:CN_sg>Beer</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Beer</ace_lexicon:PN_sg>
        <ace_lexicon:CN_pl>Beers</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Blonde -->

    <owl:Class rdf:about="&product;Blonde">
        <rdfs:subClassOf rdf:resource="&product;Beer"/>
        <ace_lexicon:CN_pl>Blondes</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Blonde</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Boat -->

    <owl:Class rdf:about="&product;Boat">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_sg>Boat</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Boats</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Bread -->

    <owl:Class rdf:about="&product;Bread">
        <rdfs:subClassOf rdf:resource="&product;Food"/>
        <ace_lexicon:CN_sg>Bread</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Breads</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Car -->

    <owl:Class rdf:about="&product;Car">
        <rdfs:subClassOf rdf:resource="&product;Vehicle"/>
        <ace_lexicon:CN_pl>Cars</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Car</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#CasualWear -->

    <owl:Class rdf:about="&product;CasualWear">
        <rdfs:subClassOf rdf:resource="&product;Clothing"/>
        <ace_lexicon:CN_pl>CasualWears</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>CasualWear</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Cheese -->

    <owl:Class rdf:about="&product;Cheese">
        <rdfs:subClassOf rdf:resource="&product;Dairy"/>
        <ace_lexicon:CN_sg>Cheese</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Cheeses</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Clothing -->

    <owl:Class rdf:about="&product;Clothing">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_sg>Clothing</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Clothings</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>Clothing</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Confectionary -->

    <owl:Class rdf:about="&product;Confectionary">
        <rdfs:subClassOf rdf:resource="&product;Food"/>
        <ace_lexicon:CN_pl>Confectionaries</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Confectionary</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#ConstructionEquipment -->

    <owl:Class rdf:about="&product;ConstructionEquipment">
        <rdfs:subClassOf rdf:resource="&product;Equipment"/>
        <ace_lexicon:CN_pl>ConstructionEquipments</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>ConstructionEquipment</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Dairy -->

    <owl:Class rdf:about="&product;Dairy">
        <rdfs:subClassOf rdf:resource="&product;Food"/>
        <ace_lexicon:CN_sg>Dairy</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Dairies</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>Dairy</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Denim -->

    <owl:Class rdf:about="&product;Denim">
        <rdfs:subClassOf rdf:resource="&product;CasualWear"/>
        <ace_lexicon:CN_sg>Denim</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Denims</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Drier -->

    <owl:Class rdf:about="&product;Drier">
        <rdfs:subClassOf rdf:resource="&product;Appliance"/>
        <ace_lexicon:CN_pl>Driers</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Drier</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Drink -->

    <owl:Class rdf:about="&product;Drink">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_pl>Drinks</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Drink</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Equipment -->

    <owl:Class rdf:about="&product;Equipment">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_sg>Equipment</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Equipments</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Fish -->

    <owl:Class rdf:about="&product;Fish">
        <rdfs:subClassOf rdf:resource="&product;Food"/>
        <ace_lexicon:CN_pl>Fish</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>Fish</ace_lexicon:PN_sg>
        <ace_lexicon:CN_sg>Fish</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Food -->

    <owl:Class rdf:about="&product;Food">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_pl>Foods</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Food</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#FootWear -->

    <owl:Class rdf:about="&product;FootWear">
        <rdfs:subClassOf rdf:resource="&product;Clothing"/>
        <ace_lexicon:CN_pl>FootWears</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>FootWear</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#FormalWear -->

    <owl:Class rdf:about="&product;FormalWear">
        <rdfs:subClassOf rdf:resource="&product;Clothing"/>
        <ace_lexicon:CN_sg>FormalWear</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>FormalWears</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Fridge -->

    <owl:Class rdf:about="&product;Fridge">
        <rdfs:subClassOf rdf:resource="&product;Appliance"/>
        <ace_lexicon:CN_sg>Fridge</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Fridges</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Fruits -->

    <owl:Class rdf:about="&product;Fruits">
        <rdfs:subClassOf rdf:resource="&product;Food"/>
        <ace_lexicon:CN_sg>Fruits</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Fruitses</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#GiftCertificate -->

    <owl:Class rdf:about="&product;GiftCertificate">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_pl>GiftCertificates</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>GiftCertificate</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#HockeyGloves -->

    <owl:Class rdf:about="&product;HockeyGloves">
        <rdfs:subClassOf rdf:resource="&product;SportsWear"/>
        <ace_lexicon:CN_sg>HockeyGloves</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>HockeyGloveses</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#HockeyJersey -->

    <owl:Class rdf:about="&product;HockeyJersey">
        <rdfs:subClassOf rdf:resource="&product;SportsWear"/>
        <ace_lexicon:CN_pl>HockeyJerseys</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>HockeyJersey</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#HockeySkates -->

    <owl:Class rdf:about="&product;HockeySkates">
        <rdfs:subClassOf rdf:resource="&product;SportsWear"/>
        <ace_lexicon:CN_sg>HockeySkates</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>HockeySkateses</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Juice -->

    <owl:Class rdf:about="&product;Juice">
        <rdfs:subClassOf rdf:resource="&product;NonAlcoholic"/>
        <ace_lexicon:CN_pl>Juices</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Juice</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Khakis -->

    <owl:Class rdf:about="&product;Khakis">
        <rdfs:subClassOf rdf:resource="&product;CasualWear"/>
        <ace_lexicon:CN_sg>Khakis</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Khakises</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Meat -->

    <owl:Class rdf:about="&product;Meat">
        <rdfs:subClassOf rdf:resource="&product;Food"/>
        <ace_lexicon:CN_sg>Meat</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Meat</ace_lexicon:PN_sg>
        <ace_lexicon:CN_pl>Meats</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Merchandise -->

    <owl:Class rdf:about="&product;Merchandise">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_pl>Merchandises</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Merchandise</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Milk -->

    <owl:Class rdf:about="&product;Milk">
        <rdfs:subClassOf rdf:resource="&product;Dairy"/>
        <ace_lexicon:CN_pl>Milks</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Milk</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Milk</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Motorcyle -->

    <owl:Class rdf:about="&product;Motorcyle">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_sg>Motorcyle</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Motorcyles</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#NonAlcoholic -->

    <owl:Class rdf:about="&product;NonAlcoholic">
        <rdfs:subClassOf rdf:resource="&product;Drink"/>
        <ace_lexicon:CN_sg>NonAlcoholic</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>NonAlcoholics</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#OutdoorsWear -->

    <owl:Class rdf:about="&product;OutdoorsWear">
        <rdfs:subClassOf rdf:resource="&product;Clothing"/>
        <ace_lexicon:CN_sg>OutdoorsWear</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>OutdoorsWears</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Pasta -->

    <owl:Class rdf:about="&product;Pasta">
        <rdfs:subClassOf rdf:resource="&product;Food"/>
        <ace_lexicon:CN_pl>Pastas</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Pasta</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Pilsener -->

    <owl:Class rdf:about="&product;Pilsener">
        <rdfs:subClassOf rdf:resource="&product;Beer"/>
        <ace_lexicon:CN_sg>Pilsener</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Pilseners</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Product -->

    <owl:Class rdf:about="&product;Product">
        <rdfs:subClassOf rdf:resource="&j.0;BaseClass"/>
        <ace_lexicon:CN_pl>Products</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Product</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>Product</ace_lexicon:PN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Sedan -->

    <owl:Class rdf:about="&product;Sedan">
        <rdfs:subClassOf rdf:resource="&product;Car"/>
        <ace_lexicon:CN_sg>Sedan</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Sedans</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Soda -->

    <owl:Class rdf:about="&product;Soda">
        <rdfs:subClassOf rdf:resource="&product;NonAlcoholic"/>
        <ace_lexicon:PN_sg>Soda</ace_lexicon:PN_sg>
        <ace_lexicon:CN_pl>Sodas</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Soda</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Spirit -->

    <owl:Class rdf:about="&product;Spirit">
        <rdfs:subClassOf rdf:resource="&product;Alcoholic"/>
        <ace_lexicon:CN_pl>Spirits</ace_lexicon:CN_pl>
        <ace_lexicon:PN_sg>Spirit</ace_lexicon:PN_sg>
        <ace_lexicon:CN_sg>Spirit</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#SportsCar -->

    <owl:Class rdf:about="&product;SportsCar">
        <rdfs:subClassOf rdf:resource="&product;Car"/>
        <ace_lexicon:CN_sg>SportsCar</ace_lexicon:CN_sg>
        <ace_lexicon:PN_sg>SportsCar</ace_lexicon:PN_sg>
        <ace_lexicon:CN_pl>SportsCars</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#SportsEquipment -->

    <owl:Class rdf:about="&product;SportsEquipment">
        <rdfs:subClassOf rdf:resource="&product;Equipment"/>
        <ace_lexicon:CN_sg>SportsEquipment</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>SportsEquipments</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#SportsWear -->

    <owl:Class rdf:about="&product;SportsWear">
        <rdfs:subClassOf rdf:resource="&product;Clothing"/>
        <ace_lexicon:CN_pl>SportsWears</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>SportsWear</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Stout -->

    <owl:Class rdf:about="&product;Stout">
        <rdfs:subClassOf rdf:resource="&product;Beer"/>
        <ace_lexicon:CN_sg>Stout</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Stouts</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Stove -->

    <owl:Class rdf:about="&product;Stove">
        <rdfs:subClassOf rdf:resource="&product;Appliance"/>
        <ace_lexicon:CN_sg>Stove</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Stoves</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Truck -->

    <owl:Class rdf:about="&product;Truck">
        <rdfs:subClassOf rdf:resource="&product;Car"/>
        <ace_lexicon:PN_sg>Truck</ace_lexicon:PN_sg>
        <ace_lexicon:CN_sg>Truck</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Trucks</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Vegetables -->

    <owl:Class rdf:about="&product;Vegetables">
        <rdfs:subClassOf rdf:resource="&product;Food"/>
        <ace_lexicon:CN_pl>Vegetableses</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Vegetables</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Vehicle -->

    <owl:Class rdf:about="&product;Vehicle">
        <rdfs:subClassOf rdf:resource="&product;Product"/>
        <ace_lexicon:CN_sg>Vehicle</ace_lexicon:CN_sg>
        <ace_lexicon:CN_pl>Vehicles</ace_lexicon:CN_pl>
    </owl:Class>
    


    <!-- http://engage/ontology/product#Washer -->

    <owl:Class rdf:about="&product;Washer">
        <rdfs:subClassOf rdf:resource="&product;Appliance"/>
        <ace_lexicon:CN_pl>Washers</ace_lexicon:CN_pl>
        <ace_lexicon:CN_sg>Washer</ace_lexicon:CN_sg>
    </owl:Class>
    


    <!-- 
    ///////////////////////////////////////////////////////////////////////////////////////
    //
    // Individuals
    //
    ///////////////////////////////////////////////////////////////////////////////////////
     -->

    


    <!-- http://engage/ontology/person360#Artemisia_Fan -->

    <owl:NamedIndividual rdf:about="&person;Artemisia_Fan">
        <rdf:type rdf:resource="&person;Person"/>
        <Age rdf:datatype="&xsd;long">19</Age>
        <ID>444</ID>
        <Profession>Student</Profession>
        <speaksLanguage>Finnish</speaksLanguage>
        <speaksLanguage>English</speaksLanguage>
        <Name>Artemisia_Fan</Name>
        <ace_lexicon:PN_sg>Artemisia_Fan</ace_lexicon:PN_sg>
        <Gender>F</Gender>
        <hasFather rdf:resource="&person;Matt_Superfan"/>
        <LivesIn rdf:resource="&place;Toronto"/>
        <WorksAt rdf:resource="&place;University_Of_Toroto"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Band -->

    <owl:NamedIndividual rdf:about="&person;Band"/>
    


    <!-- http://engage/ontology/person360#Bill_Fan -->

    <owl:NamedIndividual rdf:about="&person;Bill_Fan">
        <rdf:type rdf:resource="&person;Person"/>
        <Age rdf:datatype="&xsd;long">45</Age>
        <Profession>Electrical Engineer</Profession>
        <Name>Bill_Fan</Name>
        <ace_lexicon:PN_sg>Bill_Fan</ace_lexicon:PN_sg>
        <speaksLanguage>English</speaksLanguage>
        <Gender>M</Gender>
        <ID>555</ID>
        <hasBrother rdf:resource="&person;Wayne_Superfan"/>
        <LivesIn rdf:resource="&place;Los_Angeles"/>
        <WorksAt rdf:resource="&place;AT&amp;T"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Carl_Fan -->

    <owl:NamedIndividual rdf:about="&person;Carl_Fan">
        <rdf:type rdf:resource="&person;Person"/>
        <Age rdf:datatype="&xsd;long">18</Age>
        <ace_lexicon:PN_sg>Carl_Fan</ace_lexicon:PN_sg>
        <speaksLanguage>English</speaksLanguage>
        <Name>Carl_Fan</Name>
        <Profession>Student</Profession>
        <Gender>M</Gender>
        <ID>333</ID>
        <hasFather rdf:resource="&person;Wayne_Superfan"/>
        <LivesIn rdf:resource="&place;Calgary"/>
        <WorksAt rdf:resource="&place;University_Of_Calgary"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Coleague -->

    <owl:NamedIndividual rdf:about="&person;Coleague">
        <ace_lexicon:TV_vbg>Coleagued</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>Coleague</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>Coleague</ace_lexicon:PN_sg>
        <ace_lexicon:TV_sg>Coleagues</ace_lexicon:TV_sg>
        <owl:sameAs rdf:resource="&person;Peer"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#DeMar_DeRosen -->

    <owl:NamedIndividual rdf:about="&person;DeMar_DeRosen">
        <rdf:type rdf:resource="&person;SportsPlayer"/>
        <PlayerName>DeMar DeRosen</PlayerName>
        <SportType>Basketball</SportType>
        <ace_lexicon:PN_sg>DeMar_DeRosen</ace_lexicon:PN_sg>
        <TeamName>Raptors</TeamName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Jean_Fan -->

    <owl:NamedIndividual rdf:about="&person;Jean_Fan">
        <rdf:type rdf:resource="&person;Person"/>
        <Age rdf:datatype="&xsd;long">20</Age>
        <speaksLanguage>English</speaksLanguage>
        <speaksLanguage>French</speaksLanguage>
        <Gender>M</Gender>
        <Name>Jean_Fan</Name>
        <ace_lexicon:PN_sg>Jean_Fan</ace_lexicon:PN_sg>
        <Profession>Student</Profession>
        <ID>111</ID>
        <hasFather rdf:resource="&person;Mario_Superfan"/>
        <LivesIn rdf:resource="&place;Montreal"/>
        <WorksAt rdf:resource="&place;University_McGill"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Likes -->

    <owl:NamedIndividual rdf:about="&person;Likes">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">18</j.0:PropertyCounter>
        <ace_lexicon:PN_sg>Likes</ace_lexicon:PN_sg>
        <ace_lexicon:TV_vbg>Likesed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>Likes</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>Likeses</ace_lexicon:TV_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#LivesIn -->

    <owl:NamedIndividual rdf:about="&person;LivesIn">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">7</j.0:PropertyCounter>
        <ace_lexicon:PN_sg>LivesIn</ace_lexicon:PN_sg>
        <ace_lexicon:TV_sg>LivesIns</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>LivesIned</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>LivesIn</ace_lexicon:TV_pl>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#LoyaltyProgram -->

    <owl:NamedIndividual rdf:about="&person;LoyaltyProgram"/>
    


    <!-- http://engage/ontology/person360#LoyaltyProgram_1 -->

    <owl:NamedIndividual rdf:about="&person;LoyaltyProgram_1">
        <rdf:type rdf:resource="&person;LoyaltyProgram"/>
        <TotalLoyaltyPoints rdf:datatype="&xsd;long">100000</TotalLoyaltyPoints>
        <ace_lexicon:PN_sg>LoyaltyProgram_1</ace_lexicon:PN_sg>
        <LoyaltyTier>GOLD</LoyaltyTier>
        <LoyaltyUserId>777</LoyaltyUserId>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Marie_Fan -->

    <owl:NamedIndividual rdf:about="&person;Marie_Fan">
        <rdf:type rdf:resource="&person;Person"/>
        <Age rdf:datatype="&xsd;long">15</Age>
        <Gender rdf:datatype="&xsd;string">F</Gender>
        <speaksLanguage>French</speaksLanguage>
        <speaksLanguage>English</speaksLanguage>
        <ID>222</ID>
        <Name>Marie_Fan</Name>
        <Profession>Student</Profession>
        <ace_lexicon:PN_sg>Marie_Fan</ace_lexicon:PN_sg>
        <hasFather rdf:resource="&person;Mario_Superfan"/>
        <WorksAt rdf:resource="&place;Davis_College"/>
        <LivesIn rdf:resource="&place;Montreal"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Mario_Superfan -->

    <owl:NamedIndividual rdf:about="&person;Mario_Superfan">
        <rdf:type rdf:resource="&person;Person"/>
        <Age rdf:datatype="&xsd;long">51</Age>
        <speaksLanguage>French</speaksLanguage>
        <Name>Mario Superfan</Name>
        <FirstName>Mario</FirstName>
        <ace_lexicon:PN_sg>Mario_Superfan</ace_lexicon:PN_sg>
        <LastName>Superfan</LastName>
        <speaksLanguage>English</speaksLanguage>
        <DateOfBirth>1965-10-5</DateOfBirth>
        <Profession>Hockey Player</Profession>
        <Gender>M</Gender>
        <ID>999</ID>
        <FaceBookId>Mario_Superfan</FaceBookId>
        <hasSon rdf:resource="&person;Jean_Fan"/>
        <hasDaughter rdf:resource="&person;Marie_Fan"/>
        <LivesIn rdf:resource="&place;Montreal"/>
        <WorksAt rdf:resource="&place;Montreal_Canadiens"/>
        <Likes rdf:resource="&product;Camembert"/>
        <Likes rdf:resource="&product;Cheval_Blanc"/>
        <Owns rdf:resource="&product;Jaguar_XS"/>
        <Likes rdf:resource="&product;Sockeye_Salmon"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Matt_Superfan -->

    <owl:NamedIndividual rdf:about="&person;Matt_Superfan">
        <rdf:type rdf:resource="&person;Person"/>
        <Age rdf:datatype="&xsd;long">50</Age>
        <Address>3981 Steels Ave., Toronto,ON, CA</Address>
        <DateOfBirth>1971-02-13</DateOfBirth>
        <Profession>Hockey Player</Profession>
        <Name>Matt_Superfan</Name>
        <LastName>Superfan</LastName>
        <speaksLanguage>English</speaksLanguage>
        <ID>888</ID>
        <ace_lexicon:PN_sg>Matt_Superfan</ace_lexicon:PN_sg>
        <speaksLanguage>Swedish</speaksLanguage>
        <FirstName>Matt</FirstName>
        <Gender>M</Gender>
        <FaceBookId>Matt_Superfan</FaceBookId>
        <hasDaughter rdf:resource="&person;Artemisia_Fan"/>
        <Likes rdf:resource="&person;Phil_Kessel"/>
        <Likes rdf:resource="&person;PreferedSport_Hockey"/>
        <Likes rdf:resource="&person;SocFriend_03"/>
        <Likes rdf:resource="&person;Super_Ice_Man"/>
        <Likes rdf:resource="&person;Wayne_Superfan"/>
        <LivesIn rdf:resource="&place;Toronto"/>
        <WorksAt rdf:resource="&place;Toronto_Maple_Leafs"/>
        <Likes rdf:resource="&product;Chorizo_Saussage"/>
        <Likes rdf:resource="&product;HotDog"/>
        <Owns rdf:resource="&product;TestaRosa"/>
        <Likes rdf:resource="&product;Vodka_Finlandia"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#MusicType -->

    <owl:NamedIndividual rdf:about="&person;MusicType"/>
    


    <!-- http://engage/ontology/person360#Musician -->

    <owl:NamedIndividual rdf:about="&person;Musician"/>
    


    <!-- http://engage/ontology/person360#Owns -->

    <owl:NamedIndividual rdf:about="&person;Owns">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">1</j.0:PropertyCounter>
        <ace_lexicon:TV_sg>Ownses</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>Ownsed</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_pl>Owns</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>Owns</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#PaymentPreference -->

    <owl:NamedIndividual rdf:about="&person;PaymentPreference"/>
    


    <!-- http://engage/ontology/person360#PaymentPreference_1 -->

    <owl:NamedIndividual rdf:about="&person;PaymentPreference_1">
        <rdf:type rdf:resource="&person;PaymentPreference"/>
        <CardNumber>123456789</CardNumber>
        <CardType>VISA</CardType>
        <IssuingBank>CIBC</IssuingBank>
        <ace_lexicon:PN_sg>PaymentPreference_1</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Peer -->

    <owl:NamedIndividual rdf:about="&person;Peer">
        <ace_lexicon:TV_vbg>Peered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>Peers</ace_lexicon:TV_sg>
        <ace_lexicon:PN_sg>Peer</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>Peer</ace_lexicon:TV_pl>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Person -->

    <owl:NamedIndividual rdf:about="&person;Person">
        <j.0:ClassCounter rdf:datatype="&xsd;long">16</j.0:ClassCounter>
        <Likes rdf:resource="&person;Band"/>
        <Has rdf:resource="&person;LoyaltyProgram"/>
        <Likes rdf:resource="&person;MusicType"/>
        <Likes rdf:resource="&person;Musician"/>
        <Has rdf:resource="&person;PaymentPreference"/>
        <FamilyRelationship rdf:resource="&person;Person"/>
        <Likes rdf:resource="&person;Person"/>
        <Likes rdf:resource="&person;SocialMediaFriend"/>
        <Likes rdf:resource="&person;Sport"/>
        <Likes rdf:resource="&person;SportsPlayer"/>
        <Likes rdf:resource="&person;SportsTeam"/>
        <WorksAt rdf:resource="&person;WorkPlace"/>
        <LivesIn rdf:resource="&place;Address"/>
        <LivesIn rdf:resource="&place;City"/>
        <LivesIn rdf:resource="&place;Continent"/>
        <LivesIn rdf:resource="&place;Country"/>
        <LivesIn rdf:resource="&place;Planet"/>
        <LivesIn rdf:resource="&place;State"/>
        <Owns rdf:resource="&product;Product"/>
        <Likes rdf:resource="&product;Product"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Phil_Kessel -->

    <owl:NamedIndividual rdf:about="&person;Phil_Kessel">
        <rdf:type rdf:resource="&person;SportsPlayer"/>
        <ace_lexicon:PN_sg>Phil_Kessel</ace_lexicon:PN_sg>
        <SportType>Hockey</SportType>
        <TeamName>Maple Leafs</TeamName>
        <PlayerName>Phil Kessel</PlayerName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#PreferedSport_Baseball -->

    <owl:NamedIndividual rdf:about="&person;PreferedSport_Baseball">
        <rdf:type rdf:resource="&person;Baseball"/>
        <NumGamesAttendedSeason rdf:datatype="&xsd;long">10</NumGamesAttendedSeason>
        <NumGamesAttendedTotal rdf:datatype="&xsd;long">1000</NumGamesAttendedTotal>
        <GameSentiment>Positive</GameSentiment>
        <SportType>Baseball</SportType>
        <ace_lexicon:PN_sg>PreferedSport_Baseball</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#PreferedSport_Basketball -->

    <owl:NamedIndividual rdf:about="&person;PreferedSport_Basketball">
        <rdf:type rdf:resource="&person;Basketball"/>
        <NumGamesAttendedSeason rdf:datatype="&xsd;long">10</NumGamesAttendedSeason>
        <NumGamesAttendedTotal rdf:datatype="&xsd;long">1000</NumGamesAttendedTotal>
        <SportType>Basketball</SportType>
        <GameSentiment>Positive</GameSentiment>
        <ace_lexicon:PN_sg>PreferedSport_Basketball</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#PreferedSport_Hockey -->

    <owl:NamedIndividual rdf:about="&person;PreferedSport_Hockey">
        <rdf:type rdf:resource="&person;Hockey"/>
        <NumGamesAttendedSeason rdf:datatype="&xsd;long">10</NumGamesAttendedSeason>
        <NumGamesAttendedTotal rdf:datatype="&xsd;long">1000</NumGamesAttendedTotal>
        <NumGamesAttendedTotal rdf:datatype="&xsd;long">500</NumGamesAttendedTotal>
        <NumGamesAttendedSeason rdf:datatype="&xsd;long">8</NumGamesAttendedSeason>
        <SportType>Hockey</SportType>
        <GameSentiment>Positive</GameSentiment>
        <ace_lexicon:PN_sg>PreferedSport_Hockey</ace_lexicon:PN_sg>
        <GameSentiment>Neutral</GameSentiment>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Raptors -->

    <owl:NamedIndividual rdf:about="&person;Raptors">
        <rdf:type rdf:resource="&person;SportsTeam"/>
        <ace_lexicon:PN_sg>Raptors</ace_lexicon:PN_sg>
        <TeamName>Toronto Raptors</TeamName>
        <SportType>Basketball</SportType>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#RollingStones -->

    <owl:NamedIndividual rdf:about="&person;RollingStones">
        <rdf:type rdf:resource="&person;Band"/>
        <ace_lexicon:PN_sg>RollingStones</ace_lexicon:PN_sg>
        <BandStyle>Rock &amp; Roll</BandStyle>
        <BandName>Rolling Stones</BandName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#SocFriend_03 -->

    <owl:NamedIndividual rdf:about="&person;SocFriend_03">
        <rdf:type rdf:resource="&person;SocialMediaFriend"/>
        <SocialMediaFriendApp>Facebook</SocialMediaFriendApp>
        <ace_lexicon:PN_sg>SocFriend_03</ace_lexicon:PN_sg>
        <SocialMediaFriendName>Wayne_Superfan</SocialMediaFriendName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#SocFriend_1 -->

    <owl:NamedIndividual rdf:about="&person;SocFriend_1">
        <rdf:type rdf:resource="&person;SocialMediaFriend"/>
        <SocialMediaFriendName>Alice Of Wonderland</SocialMediaFriendName>
        <SocialMediaFriendApp>twitter</SocialMediaFriendApp>
        <ace_lexicon:PN_sg>SocFriend_1</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#SocFriend_2 -->

    <owl:NamedIndividual rdf:about="&person;SocFriend_2">
        <rdf:type rdf:resource="&person;SocialMediaFriend"/>
        <ace_lexicon:PN_sg>SocFriend_2</ace_lexicon:PN_sg>
        <SocialMediaFriendName>The Cheshire Cat</SocialMediaFriendName>
        <SocialMediaFriendApp>Facebook</SocialMediaFriendApp>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#SocFriend_3 -->

    <owl:NamedIndividual rdf:about="&person;SocFriend_3">
        <rdf:type rdf:resource="&person;SocialMediaFriend"/>
        <SocialMediaFriendName>Matt_Superfan</SocialMediaFriendName>
        <SocialMediaFriendApp>Facebook</SocialMediaFriendApp>
        <ace_lexicon:PN_sg>SocFriend_3</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#SocFriend_4 -->

    <owl:NamedIndividual rdf:about="&person;SocFriend_4">
        <rdf:type rdf:resource="&person;SocialMediaFriend"/>
        <SocialMediaFriendName>Mario_Lemieux</SocialMediaFriendName>
        <SocialMediaFriendApp>Facebook</SocialMediaFriendApp>
        <ace_lexicon:PN_sg>SocFriend_4</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#SocialMediaFriend -->

    <owl:NamedIndividual rdf:about="&person;SocialMediaFriend"/>
    


    <!-- http://engage/ontology/person360#Sport -->

    <owl:NamedIndividual rdf:about="&person;Sport"/>
    


    <!-- http://engage/ontology/person360#SportsPlayer -->

    <owl:NamedIndividual rdf:about="&person;SportsPlayer"/>
    


    <!-- http://engage/ontology/person360#SportsTeam -->

    <owl:NamedIndividual rdf:about="&person;SportsTeam"/>
    


    <!-- http://engage/ontology/person360#Super_Bat_Man -->

    <owl:NamedIndividual rdf:about="&person;Super_Bat_Man">
        <rdf:type rdf:resource="&person;SportsPlayer"/>
        <TeamName>Blue Jays</TeamName>
        <ace_lexicon:PN_sg>Super_Bat_Man</ace_lexicon:PN_sg>
        <PlayerName>Super Bat Man</PlayerName>
        <SportType>Baseball</SportType>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Super_Ice_Man -->

    <owl:NamedIndividual rdf:about="&person;Super_Ice_Man">
        <rdf:type rdf:resource="&person;SportsPlayer"/>
        <TeamName>Maple Leafs</TeamName>
        <SportType>Hockey</SportType>
        <ace_lexicon:PN_sg>Super_Ice_Man</ace_lexicon:PN_sg>
        <PlayerName>Super Ice Man</PlayerName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#Wayne_Superfan -->

    <owl:NamedIndividual rdf:about="&person;Wayne_Superfan">
        <rdf:type rdf:resource="&person;Person"/>
        <Age rdf:datatype="&xsd;long">53</Age>
        <Profession>Hockey Player</Profession>
        <FirstName>Wayne</FirstName>
        <DateOfBirth>1961-01-26</DateOfBirth>
        <Address>123 Stevens Ave., Calgary,AL, CA</Address>
        <LastName>Superfan</LastName>
        <ace_lexicon:PN_sg>Wayne_Superfan</ace_lexicon:PN_sg>
        <FaceBookId>Wayne_Superfan</FaceBookId>
        <Name>Wayne_Superfan</Name>
        <Gender>M</Gender>
        <speaksLanguage>English</speaksLanguage>
        <ID>777</ID>
        <hasBrother rdf:resource="&person;Bill_Fan"/>
        <hasSon rdf:resource="&person;Carl_Fan"/>
        <Likes rdf:resource="&person;DeMar_DeRosen"/>
        <Has rdf:resource="&person;LoyaltyProgram_1"/>
        <Likes rdf:resource="&person;Matt_Superfan"/>
        <Has rdf:resource="&person;PaymentPreference_1"/>
        <Likes rdf:resource="&person;Phil_Kessel"/>
        <Likes rdf:resource="&person;PreferedSport_Baseball"/>
        <Likes rdf:resource="&person;PreferedSport_Basketball"/>
        <Likes rdf:resource="&person;PreferedSport_Hockey"/>
        <Likes rdf:resource="&person;Raptors"/>
        <Likes rdf:resource="&person;RollingStones"/>
        <Likes rdf:resource="&person;SocFriend_1"/>
        <Likes rdf:resource="&person;SocFriend_2"/>
        <Likes rdf:resource="&person;SocFriend_3"/>
        <Likes rdf:resource="&person;SocFriend_4"/>
        <Likes rdf:resource="&person;Super_Bat_Man"/>
        <Likes rdf:resource="&person;Super_Ice_Man"/>
        <LivesIn rdf:resource="&place;Calgary"/>
        <WorksAt rdf:resource="&place;Calgary_Flames"/>
        <Likes rdf:resource="&product;BauerHockeyGloves"/>
        <Likes rdf:resource="&product;BauerHockeySkates"/>
        <Likes rdf:resource="&product;Chorizo_Saussage"/>
        <Likes rdf:resource="&product;Corona"/>
        <Owns rdf:resource="&product;DodgeRAM"/>
        <Likes rdf:resource="&product;Guiness"/>
        <Likes rdf:resource="&product;HotDog"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#WorkPlace -->

    <owl:NamedIndividual rdf:about="&person;WorkPlace"/>
    


    <!-- http://engage/ontology/person360#WorksAt -->

    <owl:NamedIndividual rdf:about="&person;WorksAt">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">8</j.0:PropertyCounter>
        <ace_lexicon:TV_sg>WorksAts</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>WorksAt</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>WorksAt</ace_lexicon:PN_sg>
        <ace_lexicon:TV_vbg>WorksAted</ace_lexicon:TV_vbg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#hasBrother -->

    <owl:NamedIndividual rdf:about="&person;hasBrother">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">2</j.0:PropertyCounter>
        <ace_lexicon:TV_vbg>hasBrothered</ace_lexicon:TV_vbg>
        <ace_lexicon:PN_sg>hasBrother</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasBrother</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>hasBrothers</ace_lexicon:TV_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#hasChild -->

    <owl:NamedIndividual rdf:about="&person;hasChild">
        <Degree rdf:datatype="&xsd;string">1</Degree>
        <ace_lexicon:TV_sg>hasChilds</ace_lexicon:TV_sg>
        <ace_lexicon:TV_pl>hasChild</ace_lexicon:TV_pl>
        <ace_lexicon:PN_sg>hasChild</ace_lexicon:PN_sg>
        <ace_lexicon:TV_vbg>hasChilded</ace_lexicon:TV_vbg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#hasDaughter -->

    <owl:NamedIndividual rdf:about="&person;hasDaughter">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">2</j.0:PropertyCounter>
        <ace_lexicon:PN_sg>hasDaughter</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasDaughter</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasDaughtered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>hasDaughters</ace_lexicon:TV_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#hasFather -->

    <owl:NamedIndividual rdf:about="&person;hasFather">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">3</j.0:PropertyCounter>
        <ace_lexicon:PN_sg>hasFather</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasFather</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>hasFathers</ace_lexicon:TV_sg>
        <ace_lexicon:TV_vbg>hasFathered</ace_lexicon:TV_vbg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#hasSister -->

    <owl:NamedIndividual rdf:about="&person;hasSister">
        <Degree rdf:datatype="&xsd;string">2</Degree>
        <ace_lexicon:TV_vbg>hasSistered</ace_lexicon:TV_vbg>
        <ace_lexicon:TV_sg>hasSisters</ace_lexicon:TV_sg>
        <ace_lexicon:PN_sg>hasSister</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasSister</ace_lexicon:TV_pl>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/person360#hasSon -->

    <owl:NamedIndividual rdf:about="&person;hasSon">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">2</j.0:PropertyCounter>
        <ace_lexicon:TV_sg>hasSons</ace_lexicon:TV_sg>
        <ace_lexicon:PN_sg>hasSon</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasSon</ace_lexicon:TV_pl>
        <ace_lexicon:TV_vbg>hasSoned</ace_lexicon:TV_vbg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Address -->

    <owl:NamedIndividual rdf:about="&place;Address">
        <place:placeBelongsTo rdf:resource="&place;City"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Africa -->

    <owl:NamedIndividual rdf:about="&place;Africa">
        <rdf:type rdf:resource="&place;Continent"/>
        <ace_lexicon:PN_sg>Africa</ace_lexicon:PN_sg>
        <place:placeName>Africa</place:placeName>
        <place:placeType>Continent</place:placeType>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Alberta -->

    <owl:NamedIndividual rdf:about="&place;Alberta">
        <rdf:type rdf:resource="&place;State"/>
        <place:placeName>Alberta</place:placeName>
        <ace_lexicon:PN_sg>Alberta</ace_lexicon:PN_sg>
        <place:placeType>State</place:placeType>
        <place:hasPlace rdf:resource="&place;Calgary"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Asia -->

    <owl:NamedIndividual rdf:about="&place;Asia">
        <rdf:type rdf:resource="&place;Continent"/>
        <place:placeType>Continent</place:placeType>
        <ace_lexicon:PN_sg>Asia</ace_lexicon:PN_sg>
        <place:placeName>Asia</place:placeName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Calgary -->

    <owl:NamedIndividual rdf:about="&place;Calgary">
        <rdf:type rdf:resource="&place;City"/>
        <place:placeType>City</place:placeType>
        <ace_lexicon:PN_sg>Calgary</ace_lexicon:PN_sg>
        <place:placeName>Calgary</place:placeName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Calgary_Flames -->

    <owl:NamedIndividual rdf:about="&place;Calgary_Flames">
        <rdf:type rdf:resource="&person;WorkPlace"/>
        <ace_lexicon:PN_sg>Calgary_Flames</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#California -->

    <owl:NamedIndividual rdf:about="&place;California">
        <rdf:type rdf:resource="&place;State"/>
        <place:placeName>California</place:placeName>
        <ace_lexicon:PN_sg>California</ace_lexicon:PN_sg>
        <place:placeType>State</place:placeType>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Canada -->

    <owl:NamedIndividual rdf:about="&place;Canada">
        <rdf:type rdf:resource="&place;Country"/>
        <place:placeName>Canada</place:placeName>
        <place:placeType>Country</place:placeType>
        <ace_lexicon:PN_sg>Canada</ace_lexicon:PN_sg>
        <place:hasPlace rdf:resource="&place;Alberta"/>
        <place:hasPlace rdf:resource="&place;Ontario"/>
        <place:hasPlace rdf:resource="&place;Quebec"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#City -->

    <owl:NamedIndividual rdf:about="&place;City">
        <j.0:ClassCounter rdf:datatype="&xsd;long">4</j.0:ClassCounter>
        <place:hasPlace rdf:resource="&place;Address"/>
        <place:placeBelongsTo rdf:resource="&place;Region"/>
        <place:placeBelongsTo rdf:resource="&place;State"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Continent -->

    <owl:NamedIndividual rdf:about="&place;Continent">
        <j.0:ClassCounter rdf:datatype="&xsd;long">5</j.0:ClassCounter>
        <place:hasPlace rdf:resource="&place;Country"/>
        <place:placeBelongsTo rdf:resource="&place;Planet"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Country -->

    <owl:NamedIndividual rdf:about="&place;Country">
        <j.0:ClassCounter rdf:datatype="&xsd;long">4</j.0:ClassCounter>
        <place:placeBelongsTo rdf:resource="&place;Continent"/>
        <place:hasPlace rdf:resource="&place;State"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Davis_College -->

    <owl:NamedIndividual rdf:about="&place;Davis_College">
        <rdf:type rdf:resource="&person;WorkPlace"/>
        <ace_lexicon:PN_sg>Davis_College</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Earth -->

    <owl:NamedIndividual rdf:about="&place;Earth">
        <rdf:type rdf:resource="&place;Planet"/>
        <ace_lexicon:PN_sg>Earth</ace_lexicon:PN_sg>
        <place:placeName>Earth</place:placeName>
        <place:placeType>Planet</place:placeType>
        <place:hasPlace rdf:resource="&place;Africa"/>
        <place:hasPlace rdf:resource="&place;Asia"/>
        <place:hasPlace rdf:resource="&place;Europe"/>
        <place:hasPlace rdf:resource="&place;North_America"/>
        <place:hasPlace rdf:resource="&place;South_America"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Europe -->

    <owl:NamedIndividual rdf:about="&place;Europe">
        <rdf:type rdf:resource="&place;Continent"/>
        <ace_lexicon:PN_sg>Europe</ace_lexicon:PN_sg>
        <place:placeType>Continent</place:placeType>
        <place:placeName>Europe</place:placeName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Los_Angeles -->

    <owl:NamedIndividual rdf:about="&place;Los_Angeles">
        <rdf:type rdf:resource="&place;City"/>
        <place:placeName>Los Angeles</place:placeName>
        <place:placeType>City</place:placeType>
        <ace_lexicon:PN_sg>Los_Angeles</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Mexico -->

    <owl:NamedIndividual rdf:about="&place;Mexico">
        <rdf:type rdf:resource="&place;Country"/>
        <ace_lexicon:PN_sg>Mexico</ace_lexicon:PN_sg>
        <place:placeType>Country</place:placeType>
        <place:placeName>Mexico</place:placeName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Montreal -->

    <owl:NamedIndividual rdf:about="&place;Montreal">
        <rdf:type rdf:resource="&place;City"/>
        <place:placeType>City</place:placeType>
        <ace_lexicon:PN_sg>Montreal</ace_lexicon:PN_sg>
        <place:placeName>Montreal</place:placeName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Montreal_Canadiens -->

    <owl:NamedIndividual rdf:about="&place;Montreal_Canadiens">
        <rdf:type rdf:resource="&person;WorkPlace"/>
        <ace_lexicon:PN_sg>Montreal_Canadiens</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#North_America -->

    <owl:NamedIndividual rdf:about="&place;North_America">
        <rdf:type rdf:resource="&place;Continent"/>
        <place:placeType>Continent</place:placeType>
        <ace_lexicon:PN_sg>North_America</ace_lexicon:PN_sg>
        <place:placeName>North America</place:placeName>
        <place:hasPlace rdf:resource="&place;Canada"/>
        <place:hasPlace rdf:resource="&place;Mexico"/>
        <place:hasPlace rdf:resource="&place;United_States"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Ontario -->

    <owl:NamedIndividual rdf:about="&place;Ontario">
        <rdf:type rdf:resource="&place;State"/>
        <ace_lexicon:PN_sg>Ontario</ace_lexicon:PN_sg>
        <place:placeName>Ontario</place:placeName>
        <place:placeType>State</place:placeType>
        <place:hasPlace rdf:resource="&place;Toronto"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#PlaceType -->

    <owl:NamedIndividual rdf:about="&place;PlaceType">
        <place:hasPlace rdf:resource="&place;PlaceType"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Planet -->

    <owl:NamedIndividual rdf:about="&place;Planet">
        <j.0:ClassCounter rdf:datatype="&xsd;long">1</j.0:ClassCounter>
        <place:hasPlace rdf:resource="&place;Continent"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Quebec -->

    <owl:NamedIndividual rdf:about="&place;Quebec">
        <rdf:type rdf:resource="&place;State"/>
        <place:placeType>State</place:placeType>
        <ace_lexicon:PN_sg>Quebec</ace_lexicon:PN_sg>
        <place:placeName>Quebec</place:placeName>
        <place:hasPlace rdf:resource="&place;Montreal"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Region -->

    <owl:NamedIndividual rdf:about="&place;Region">
        <place:hasPlace rdf:resource="&place;City"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#South_America -->

    <owl:NamedIndividual rdf:about="&place;South_America">
        <rdf:type rdf:resource="&place;Continent"/>
        <ace_lexicon:PN_sg>South_America</ace_lexicon:PN_sg>
        <place:placeName>South America</place:placeName>
        <place:placeType>Continent</place:placeType>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#State -->

    <owl:NamedIndividual rdf:about="&place;State">
        <j.0:ClassCounter rdf:datatype="&xsd;long">3</j.0:ClassCounter>
        <place:placeBelongsTo rdf:resource="&place;Country"/>
        <place:hasPlace rdf:resource="&place;Region"/>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Toronto -->

    <owl:NamedIndividual rdf:about="&place;Toronto">
        <rdf:type rdf:resource="&place;City"/>
        <place:placeName>Toronto</place:placeName>
        <place:placeType>City</place:placeType>
        <ace_lexicon:PN_sg>Toronto</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#Toronto_Maple_Leafs -->

    <owl:NamedIndividual rdf:about="&place;Toronto_Maple_Leafs">
        <rdf:type rdf:resource="&person;WorkPlace"/>
        <ace_lexicon:PN_sg>Toronto_Maple_Leafs</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#United_States -->

    <owl:NamedIndividual rdf:about="&place;United_States">
        <rdf:type rdf:resource="&place;Country"/>
        <place:placeType>Country</place:placeType>
        <ace_lexicon:PN_sg>United_States</ace_lexicon:PN_sg>
        <place:placeName>United States</place:placeName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#University_McGill -->

    <owl:NamedIndividual rdf:about="&place;University_McGill">
        <rdf:type rdf:resource="&person;WorkPlace"/>
        <ace_lexicon:PN_sg>University_McGill</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#University_Of_Calgary -->

    <owl:NamedIndividual rdf:about="&place;University_Of_Calgary">
        <rdf:type rdf:resource="&person;WorkPlace"/>
        <ace_lexicon:PN_sg>University_Of_Calgary</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#University_Of_Toroto -->

    <owl:NamedIndividual rdf:about="&place;University_Of_Toroto">
        <rdf:type rdf:resource="&person;WorkPlace"/>
        <ace_lexicon:PN_sg>University_Of_Toroto</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#hasPlace -->

    <owl:NamedIndividual rdf:about="&place;hasPlace">
        <j.0:PropertyCounter rdf:datatype="&xsd;long">14</j.0:PropertyCounter>
        <ace_lexicon:TV_vbg>hasPlaced</ace_lexicon:TV_vbg>
        <ace_lexicon:PN_sg>hasPlace</ace_lexicon:PN_sg>
        <ace_lexicon:TV_pl>hasPlace</ace_lexicon:TV_pl>
        <ace_lexicon:TV_sg>hasPlaces</ace_lexicon:TV_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/place#AT&amp;T -->

    <owl:NamedIndividual rdf:about="&place;AT&amp;T">
        <rdf:type rdf:resource="&person;WorkPlace"/>
        <ace_lexicon:PN_sg>T</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Appliance -->

    <owl:NamedIndividual rdf:about="&product;Appliance">
        <j.0:ClassCounter rdf:datatype="&xsd;long">2</j.0:ClassCounter>
        <product:ProductMadeIn>US</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#BauerHockeyGloves -->

    <owl:NamedIndividual rdf:about="&product;BauerHockeyGloves">
        <rdf:type rdf:resource="&product;HockeyGloves"/>
        <product:ProductName>Bauer Hockey Gloves</product:ProductName>
        <product:ProductBrand>Bauer</product:ProductBrand>
        <ace_lexicon:PN_sg>BauerHockeyGloves</ace_lexicon:PN_sg>
        <product:ItemSize>L</product:ItemSize>
        <product:ProductSerialNumber>hg444</product:ProductSerialNumber>
        <product:ProductType>HockeyGloves</product:ProductType>
        <product:ProductMadeIn>US</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#BauerHockeySkates -->

    <owl:NamedIndividual rdf:about="&product;BauerHockeySkates">
        <rdf:type rdf:resource="&product;HockeySkates"/>
        <product:ItemSize>L</product:ItemSize>
        <product:ProductSerialNumber>hk444</product:ProductSerialNumber>
        <product:ProductName>Bauer Hockey Skates</product:ProductName>
        <product:ProductMadeIn>US</product:ProductMadeIn>
        <product:ProductBrand>Bauer</product:ProductBrand>
        <ace_lexicon:PN_sg>BauerHockeySkates</ace_lexicon:PN_sg>
        <product:ProductType>HockeySkates</product:ProductType>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Beer -->

    <owl:NamedIndividual rdf:about="&product;Beer">
        <j.0:ClassCounter rdf:datatype="&xsd;long">5</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Camembert -->

    <owl:NamedIndividual rdf:about="&product;Camembert">
        <rdf:type rdf:resource="&product;Cheese"/>
        <product:ProductPrice rdf:datatype="&xsd;double">15.99</product:ProductPrice>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-07</product:ProductExpiryDate>
        <product:ProductName>Camembert</product:ProductName>
        <product:ProductSerialNumber>12345tg</product:ProductSerialNumber>
        <product:ProductBrand>President</product:ProductBrand>
        <product:ProductMadeIn>France</product:ProductMadeIn>
        <ace_lexicon:PN_sg>Camembert</ace_lexicon:PN_sg>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Cheval_Blanc -->

    <owl:NamedIndividual rdf:about="&product;Cheval_Blanc">
        <rdf:type rdf:resource="&product;Blonde"/>
        <product:ProductDiscount rdf:datatype="&xsd;long">15</product:ProductDiscount>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <product:ProductMadeIn>Canada</product:ProductMadeIn>
        <ace_lexicon:PN_sg>Cheval_Blanc</ace_lexicon:PN_sg>
        <product:ProductSerialNumber>1234y4xcb</product:ProductSerialNumber>
        <product:ProductName>ChevalBlanc</product:ProductName>
        <product:ProductBrand>Brewpub</product:ProductBrand>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Chorizo_Saussage -->

    <owl:NamedIndividual rdf:about="&product;Chorizo_Saussage">
        <rdf:type rdf:resource="&product;Meat"/>
        <product:ProductPrice rdf:datatype="&xsd;double">10.99</product:ProductPrice>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <product:ProductName>ChorizoSaussage</product:ProductName>
        <product:ProductSerialNumber>33-22-rt</product:ProductSerialNumber>
        <product:ProductBrand>MapleLeaf Farms</product:ProductBrand>
        <ace_lexicon:PN_sg>Chorizo_Saussage</ace_lexicon:PN_sg>
        <product:ProductMadeIn>Canada</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Clothing -->

    <owl:NamedIndividual rdf:about="&product;Clothing">
        <j.0:ClassCounter rdf:datatype="&xsd;long">3</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Coke -->

    <owl:NamedIndividual rdf:about="&product;Coke">
        <rdf:type rdf:resource="&product;Soda"/>
        <product:ProductBrand>CocaCola</product:ProductBrand>
        <ace_lexicon:PN_sg>Coke</ace_lexicon:PN_sg>
        <product:ProductName>Coke</product:ProductName>
        <product:ProductMadeIn>US</product:ProductMadeIn>
        <product:ProductSerialNumber>1234zrt</product:ProductSerialNumber>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Corona -->

    <owl:NamedIndividual rdf:about="&product;Corona">
        <rdf:type rdf:resource="&product;Pilsener"/>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <ace_lexicon:PN_sg>Corona</ace_lexicon:PN_sg>
        <product:ProductSerialNumber>mx447</product:ProductSerialNumber>
        <product:ProductBrand>Corona</product:ProductBrand>
        <product:ProductName>Corona</product:ProductName>
        <product:ProductMadeIn>MEX</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Dairy -->

    <owl:NamedIndividual rdf:about="&product;Dairy">
        <j.0:ClassCounter rdf:datatype="&xsd;long">2</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#DodgeRAM -->

    <owl:NamedIndividual rdf:about="&product;DodgeRAM">
        <rdf:type rdf:resource="&product;Truck"/>
        <product:ProductSerialNumber>1234zc4vb333</product:ProductSerialNumber>
        <ace_lexicon:PN_sg>DodgeRAM</ace_lexicon:PN_sg>
        <product:ProductBrand>Tata</product:ProductBrand>
        <product:ProductMadeIn>US</product:ProductMadeIn>
        <product:ProductName>Jaguar XS</product:ProductName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Fish -->

    <owl:NamedIndividual rdf:about="&product;Fish">
        <j.0:ClassCounter rdf:datatype="&xsd;long">1</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#GE-34-D -->

    <owl:NamedIndividual rdf:about="&product;GE-34-D">
        <rdf:type rdf:resource="&product;Drier"/>
        <product:ProductMadeIn>US</product:ProductMadeIn>
        <product:ProductBrand>GeneralElectric</product:ProductBrand>
        <product:ProductName>GE 34 D</product:ProductName>
        <ace_lexicon:PN_sg>GE-34-D</ace_lexicon:PN_sg>
        <product:ProductSerialNumber>12yyy</product:ProductSerialNumber>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#GE-34-W -->

    <owl:NamedIndividual rdf:about="&product;GE-34-W">
        <rdf:type rdf:resource="&product;Washer"/>
        <product:ProductBrand>GeneralElectric</product:ProductBrand>
        <product:ProductName>GE 34 W</product:ProductName>
        <ace_lexicon:PN_sg>GE-34-W</ace_lexicon:PN_sg>
        <product:ProductSerialNumber>123xxx</product:ProductSerialNumber>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Guiness -->

    <owl:NamedIndividual rdf:about="&product;Guiness">
        <rdf:type rdf:resource="&product;Stout"/>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <product:ProductMadeIn>EU</product:ProductMadeIn>
        <product:ProductBrand>Guiness</product:ProductBrand>
        <product:ProductSerialNumber>1234y4</product:ProductSerialNumber>
        <ace_lexicon:PN_sg>Guiness</ace_lexicon:PN_sg>
        <product:ProductName>Guiness</product:ProductName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#HotDog -->

    <owl:NamedIndividual rdf:about="&product;HotDog">
        <rdf:type rdf:resource="&product;Meat"/>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <product:ProductDiscount rdf:datatype="&xsd;long">25</product:ProductDiscount>
        <product:ProductPrice rdf:datatype="&xsd;double">4.99</product:ProductPrice>
        <product:ProductBrand>MapleLeaf Farms</product:ProductBrand>
        <product:ProductSerialNumber>33-22-ht</product:ProductSerialNumber>
        <product:ProductName>HotDog</product:ProductName>
        <ace_lexicon:PN_sg>HotDog</ace_lexicon:PN_sg>
        <product:ProductMadeIn>Canada</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Italian_Saussage -->

    <owl:NamedIndividual rdf:about="&product;Italian_Saussage">
        <rdf:type rdf:resource="&product;Meat"/>
        <product:ProductDiscount rdf:datatype="&xsd;long">10</product:ProductDiscount>
        <product:ProductPrice rdf:datatype="&xsd;double">10.99</product:ProductPrice>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <product:ProductSerialNumber>33-22-rt</product:ProductSerialNumber>
        <product:ProductName>ChorizoSaussage</product:ProductName>
        <ace_lexicon:PN_sg>Italian_Saussage</ace_lexicon:PN_sg>
        <product:ProductBrand>MapleLeaf Farms</product:ProductBrand>
        <product:ProductMadeIn>Canada</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Jaguar_XS -->

    <owl:NamedIndividual rdf:about="&product;Jaguar_XS">
        <rdf:type rdf:resource="&product;SportsCar"/>
        <product:ProductName>Jaguar XS</product:ProductName>
        <product:ProductBrand>Tata</product:ProductBrand>
        <product:ProductSerialNumber>1234zc4vb333</product:ProductSerialNumber>
        <ace_lexicon:PN_sg>Jaguar_XS</ace_lexicon:PN_sg>
        <product:ProductMadeIn>UK</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Labatts -->

    <owl:NamedIndividual rdf:about="&product;Labatts">
        <rdf:type rdf:resource="&product;Pilsener"/>
        <product:ProductDiscount rdf:datatype="&xsd;long">15</product:ProductDiscount>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <product:ProductSerialNumber>1234y4xx</product:ProductSerialNumber>
        <product:ProductMadeIn>Canada</product:ProductMadeIn>
        <product:ProductBrand>Labatts Brewery</product:ProductBrand>
        <ace_lexicon:PN_sg>Labatts</ace_lexicon:PN_sg>
        <product:ProductName>Labatts</product:ProductName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Levis501 -->

    <owl:NamedIndividual rdf:about="&product;Levis501">
        <rdf:type rdf:resource="&product;Denim"/>
        <product:ProductBrand>Levi Strauss</product:ProductBrand>
        <ace_lexicon:PN_sg>Levis501</ace_lexicon:PN_sg>
        <product:ProductSerialNumber>lv501</product:ProductSerialNumber>
        <product:ProductMadeIn>US</product:ProductMadeIn>
        <product:ProductName>Levis 501</product:ProductName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Meat -->

    <owl:NamedIndividual rdf:about="&product;Meat">
        <j.0:ClassCounter rdf:datatype="&xsd;long">3</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Milk -->

    <owl:NamedIndividual rdf:about="&product;Milk">
        <rdf:type rdf:resource="&product;Dairy"/>
        <product:ProductPrice rdf:datatype="&xsd;double">2.99</product:ProductPrice>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <product:ProductSerialNumber>12345</product:ProductSerialNumber>
        <product:ProductBrand>GayLea</product:ProductBrand>
        <product:ProductMadeIn>US</product:ProductMadeIn>
        <product:ProductName>Milk</product:ProductName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Pepsi -->

    <owl:NamedIndividual rdf:about="&product;Pepsi">
        <rdf:type rdf:resource="&product;Soda"/>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <ace_lexicon:PN_sg>Pepsi</ace_lexicon:PN_sg>
        <product:ProductBrand>Pepsi</product:ProductBrand>
        <product:ProductName>Pepsi</product:ProductName>
        <product:ProductSerialNumber>1234zc4</product:ProductSerialNumber>
        <product:ProductMadeIn>US</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Product -->

    <owl:NamedIndividual rdf:about="&product;Product"/>
    


    <!-- http://engage/ontology/product#Sockeye_Salmon -->

    <owl:NamedIndividual rdf:about="&product;Sockeye_Salmon">
        <rdf:type rdf:resource="&product;Fish"/>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <product:ProductBrand>MapleLeaf Farms</product:ProductBrand>
        <product:ProductSerialNumber>33-22-xv</product:ProductSerialNumber>
        <product:ProductMadeIn>Canada</product:ProductMadeIn>
        <ace_lexicon:PN_sg>Sockeye_Salmon</ace_lexicon:PN_sg>
        <product:ProductName>Sockeye Salmon</product:ProductName>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Soda -->

    <owl:NamedIndividual rdf:about="&product;Soda">
        <j.0:ClassCounter rdf:datatype="&xsd;long">2</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Spirit -->

    <owl:NamedIndividual rdf:about="&product;Spirit">
        <j.0:ClassCounter rdf:datatype="&xsd;long">1</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#SportsCar -->

    <owl:NamedIndividual rdf:about="&product;SportsCar">
        <j.0:ClassCounter rdf:datatype="&xsd;long">2</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#TestaRosa -->

    <owl:NamedIndividual rdf:about="&product;TestaRosa">
        <rdf:type rdf:resource="&product;SportsCar"/>
        <product:ProductMadeIn>IT</product:ProductMadeIn>
        <product:ProductName>TestaRosa</product:ProductName>
        <ace_lexicon:PN_sg>TestaRosa</ace_lexicon:PN_sg>
        <product:ProductBrand>Ferrarri</product:ProductBrand>
        <product:ProductSerialNumber>1234zc4vb444</product:ProductSerialNumber>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Truck -->

    <owl:NamedIndividual rdf:about="&product;Truck">
        <j.0:ClassCounter rdf:datatype="&xsd;long">1</j.0:ClassCounter>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Uruquell -->

    <owl:NamedIndividual rdf:about="&product;Uruquell">
        <rdf:type rdf:resource="&product;Pilsener"/>
        <product:ProductExpiryDate rdf:datatype="&xsd;date">2014-12-06</product:ProductExpiryDate>
        <ace_lexicon:PN_sg>Uruquell</ace_lexicon:PN_sg>
        <product:ProductName>Uruquell</product:ProductName>
        <product:ProductBrand>PivoVar</product:ProductBrand>
        <product:ProductSerialNumber>1234y4</product:ProductSerialNumber>
        <product:ProductMadeIn>CZ</product:ProductMadeIn>
    </owl:NamedIndividual>
    


    <!-- http://engage/ontology/product#Vodka_Finlandia -->

    <owl:NamedIndividual rdf:about="&product;Vodka_Finlandia">
        <rdf:type rdf:resource="&product;Spirit"/>
        <product:ProductName>Vodka Finlandia</product:ProductName>
        <product:ProductBrand>ABS</product:ProductBrand>
        <ace_lexicon:PN_sg>Vodka_Finlandia</ace_lexicon:PN_sg>
        <product:ProductMadeIn>FIN</product:ProductMadeIn>
        <product:ProductSerialNumber>1234y-vf</product:ProductSerialNumber>
    </owl:NamedIndividual>
</rdf:RDF>



<!-- Generated by the OWL API (version 3.5.0) http://owlapi.sourceforge.net -->

