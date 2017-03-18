
package org.universAAL.commerce.ustore.tools;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.universAAL.commerce.ustore.tools package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UAALException_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "uAALException");
    private final static QName _GetPurchasedAALServicesResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getPurchasedAALServicesResponse");
    private final static QName _GetUpdatesForAALServices_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getUpdatesForAALServices");
    private final static QName _GetUserProfileResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getUserProfileResponse");
    private final static QName _GetSessionKeyResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getSessionKeyResponse");
    private final static QName _RegisterDeployManagerResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "registerDeployManagerResponse");
    private final static QName _GetPurchasedAALServices_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getPurchasedAALServices");
    private final static QName _GetUserProfile_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getUserProfile");
    private final static QName _GetSessionKey_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getSessionKey");
    private final static QName _PurchaseFreeAALServiceResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "purchaseFreeAALServiceResponse");
    private final static QName _PurchaseFreeAALService_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "purchaseFreeAALService");
    private final static QName _RegisterDeployManager_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "registerDeployManager");
    private final static QName _GetUpdatesForAALServicesResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getUpdatesForAALServicesResponse");
    private final static QName _GetFreeAALServices_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getFreeAALServices");
    private final static QName _GetFreeAALServicesResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/", "getFreeAALServicesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.universAAL.commerce.ustore.tools
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PurchaseFreeAALService }
     * 
     */
    public PurchaseFreeAALService createPurchaseFreeAALService() {
        return new PurchaseFreeAALService();
    }

    /**
     * Create an instance of {@link RegisterDeployManager }
     * 
     */
    public RegisterDeployManager createRegisterDeployManager() {
        return new RegisterDeployManager();
    }

    /**
     * Create an instance of {@link GetUpdatesForAALServicesResponse }
     * 
     */
    public GetUpdatesForAALServicesResponse createGetUpdatesForAALServicesResponse() {
        return new GetUpdatesForAALServicesResponse();
    }

    /**
     * Create an instance of {@link GetFreeAALServicesResponse }
     * 
     */
    public GetFreeAALServicesResponse createGetFreeAALServicesResponse() {
        return new GetFreeAALServicesResponse();
    }

    /**
     * Create an instance of {@link GetFreeAALServices }
     * 
     */
    public GetFreeAALServices createGetFreeAALServices() {
        return new GetFreeAALServices();
    }

    /**
     * Create an instance of {@link GetUserProfileResponse }
     * 
     */
    public GetUserProfileResponse createGetUserProfileResponse() {
        return new GetUserProfileResponse();
    }

    /**
     * Create an instance of {@link UAALException }
     * 
     */
    public UAALException createUAALException() {
        return new UAALException();
    }

    /**
     * Create an instance of {@link GetPurchasedAALServicesResponse }
     * 
     */
    public GetPurchasedAALServicesResponse createGetPurchasedAALServicesResponse() {
        return new GetPurchasedAALServicesResponse();
    }

    /**
     * Create an instance of {@link GetUpdatesForAALServices }
     * 
     */
    public GetUpdatesForAALServices createGetUpdatesForAALServices() {
        return new GetUpdatesForAALServices();
    }

    /**
     * Create an instance of {@link RegisterDeployManagerResponse }
     * 
     */
    public RegisterDeployManagerResponse createRegisterDeployManagerResponse() {
        return new RegisterDeployManagerResponse();
    }

    /**
     * Create an instance of {@link GetSessionKeyResponse }
     * 
     */
    public GetSessionKeyResponse createGetSessionKeyResponse() {
        return new GetSessionKeyResponse();
    }

    /**
     * Create an instance of {@link GetSessionKey }
     * 
     */
    public GetSessionKey createGetSessionKey() {
        return new GetSessionKey();
    }

    /**
     * Create an instance of {@link GetUserProfile }
     * 
     */
    public GetUserProfile createGetUserProfile() {
        return new GetUserProfile();
    }

    /**
     * Create an instance of {@link GetPurchasedAALServices }
     * 
     */
    public GetPurchasedAALServices createGetPurchasedAALServices() {
        return new GetPurchasedAALServices();
    }

    /**
     * Create an instance of {@link PurchaseFreeAALServiceResponse }
     * 
     */
    public PurchaseFreeAALServiceResponse createPurchaseFreeAALServiceResponse() {
        return new PurchaseFreeAALServiceResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UAALException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "uAALException")
    public JAXBElement<UAALException> createUAALException(UAALException value) {
        return new JAXBElement<UAALException>(_UAALException_QNAME, UAALException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPurchasedAALServicesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getPurchasedAALServicesResponse")
    public JAXBElement<GetPurchasedAALServicesResponse> createGetPurchasedAALServicesResponse(GetPurchasedAALServicesResponse value) {
        return new JAXBElement<GetPurchasedAALServicesResponse>(_GetPurchasedAALServicesResponse_QNAME, GetPurchasedAALServicesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUpdatesForAALServices }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getUpdatesForAALServices")
    public JAXBElement<GetUpdatesForAALServices> createGetUpdatesForAALServices(GetUpdatesForAALServices value) {
        return new JAXBElement<GetUpdatesForAALServices>(_GetUpdatesForAALServices_QNAME, GetUpdatesForAALServices.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserProfileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getUserProfileResponse")
    public JAXBElement<GetUserProfileResponse> createGetUserProfileResponse(GetUserProfileResponse value) {
        return new JAXBElement<GetUserProfileResponse>(_GetUserProfileResponse_QNAME, GetUserProfileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionKeyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getSessionKeyResponse")
    public JAXBElement<GetSessionKeyResponse> createGetSessionKeyResponse(GetSessionKeyResponse value) {
        return new JAXBElement<GetSessionKeyResponse>(_GetSessionKeyResponse_QNAME, GetSessionKeyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterDeployManagerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "registerDeployManagerResponse")
    public JAXBElement<RegisterDeployManagerResponse> createRegisterDeployManagerResponse(RegisterDeployManagerResponse value) {
        return new JAXBElement<RegisterDeployManagerResponse>(_RegisterDeployManagerResponse_QNAME, RegisterDeployManagerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPurchasedAALServices }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getPurchasedAALServices")
    public JAXBElement<GetPurchasedAALServices> createGetPurchasedAALServices(GetPurchasedAALServices value) {
        return new JAXBElement<GetPurchasedAALServices>(_GetPurchasedAALServices_QNAME, GetPurchasedAALServices.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserProfile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getUserProfile")
    public JAXBElement<GetUserProfile> createGetUserProfile(GetUserProfile value) {
        return new JAXBElement<GetUserProfile>(_GetUserProfile_QNAME, GetUserProfile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionKey }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getSessionKey")
    public JAXBElement<GetSessionKey> createGetSessionKey(GetSessionKey value) {
        return new JAXBElement<GetSessionKey>(_GetSessionKey_QNAME, GetSessionKey.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PurchaseFreeAALServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "purchaseFreeAALServiceResponse")
    public JAXBElement<PurchaseFreeAALServiceResponse> createPurchaseFreeAALServiceResponse(PurchaseFreeAALServiceResponse value) {
        return new JAXBElement<PurchaseFreeAALServiceResponse>(_PurchaseFreeAALServiceResponse_QNAME, PurchaseFreeAALServiceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PurchaseFreeAALService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "purchaseFreeAALService")
    public JAXBElement<PurchaseFreeAALService> createPurchaseFreeAALService(PurchaseFreeAALService value) {
        return new JAXBElement<PurchaseFreeAALService>(_PurchaseFreeAALService_QNAME, PurchaseFreeAALService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterDeployManager }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "registerDeployManager")
    public JAXBElement<RegisterDeployManager> createRegisterDeployManager(RegisterDeployManager value) {
        return new JAXBElement<RegisterDeployManager>(_RegisterDeployManager_QNAME, RegisterDeployManager.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUpdatesForAALServicesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getUpdatesForAALServicesResponse")
    public JAXBElement<GetUpdatesForAALServicesResponse> createGetUpdatesForAALServicesResponse(GetUpdatesForAALServicesResponse value) {
        return new JAXBElement<GetUpdatesForAALServicesResponse>(_GetUpdatesForAALServicesResponse_QNAME, GetUpdatesForAALServicesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFreeAALServices }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getFreeAALServices")
    public JAXBElement<GetFreeAALServices> createGetFreeAALServices(GetFreeAALServices value) {
        return new JAXBElement<GetFreeAALServices>(_GetFreeAALServices_QNAME, GetFreeAALServices.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFreeAALServicesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getFreeAALServicesResponse")
    public JAXBElement<GetFreeAALServicesResponse> createGetFreeAALServicesResponse(GetFreeAALServicesResponse value) {
        return new JAXBElement<GetFreeAALServicesResponse>(_GetFreeAALServicesResponse_QNAME, GetFreeAALServicesResponse.class, null, value);
    }

}
