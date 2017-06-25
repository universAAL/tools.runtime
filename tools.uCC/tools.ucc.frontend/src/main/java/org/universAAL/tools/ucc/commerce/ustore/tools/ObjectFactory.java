
package org.universAAL.tools.ucc.commerce.ustore.tools;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.universAAL.commerce.ustore.tools
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _USTORE_Exception_QNAME = new QName("http://tools.ustore.commerce.universaal.org/",
			"uStoreException");
	private final static QName _GetPurchasedServicesResponse_QNAME = new QName(
			"http://tools.ustore.commerce.universaal.org/", "getPurchasedServicesResponse");
	private final static QName _GetUpdatesForServices_QNAME = new QName(
			"http://tools.ustore.commerce.universaal.org/", "getUpdatesForServices");
	private final static QName _GetUserProfileResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/",
			"getUserProfileResponse");
	private final static QName _GetSessionKeyResponse_QNAME = new QName("http://tools.ustore.commerce.universaal.org/",
			"getSessionKeyResponse");
	private final static QName _RegisterDeployManagerResponse_QNAME = new QName(
			"http://tools.ustore.commerce.universaal.org/", "registerDeployManagerResponse");
	private final static QName _GetPurchasedServices_QNAME = new QName(
			"http://tools.ustore.commerce.universaal.org/", "getPurchasedServices");
	private final static QName _GetUserProfile_QNAME = new QName("http://tools.ustore.commerce.universaal.org/",
			"getUserProfile");
	private final static QName _GetSessionKey_QNAME = new QName("http://tools.ustore.commerce.universaal.org/",
			"getSessionKey");
	private final static QName _PurchaseFreeServiceResponse_QNAME = new QName(
			"http://tools.ustore.commerce.universaal.org/", "purchaseFreeServiceResponse");
	private final static QName _PurchaseFreeService_QNAME = new QName("http://tools.ustore.commerce.universaal.org/",
			"purchaseFreeService");
	private final static QName _RegisterDeployManager_QNAME = new QName("http://tools.ustore.commerce.universaal.org/",
			"registerDeployManager");
	private final static QName _GetUpdatesForServicesResponse_QNAME = new QName(
			"http://tools.ustore.commerce.universaal.org/", "getUpdatesForServicesResponse");
	private final static QName _GetFreeServices_QNAME = new QName("http://tools.ustore.commerce.universaal.org/",
			"getFreeServices");
	private final static QName _GetFreeServicesResponse_QNAME = new QName(
			"http://tools.ustore.commerce.universaal.org/", "getFreeServicesResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: org.universAAL.commerce.ustore.tools
	 *
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link PurchaseFreeService }
	 *
	 */
	public PurchaseFreeService createPurchaseFreeService() {
		return new PurchaseFreeService();
	}

	/**
	 * Create an instance of {@link RegisterDeployManager }
	 *
	 */
	public RegisterDeployManager createRegisterDeployManager() {
		return new RegisterDeployManager();
	}

	/**
	 * Create an instance of {@link GetUpdatesForServicesResponse }
	 *
	 */
	public GetUpdatesForServicesResponse createGetUpdatesForServicesResponse() {
		return new GetUpdatesForServicesResponse();
	}

	/**
	 * Create an instance of {@link GetFreeServicesResponse }
	 *
	 */
	public GetFreeServicesResponse createGetFreeServicesResponse() {
		return new GetFreeServicesResponse();
	}

	/**
	 * Create an instance of {@link GetFreeServices }
	 *
	 */
	public GetFreeServices createGetFreeServices() {
		return new GetFreeServices();
	}

	/**
	 * Create an instance of {@link GetUserProfileResponse }
	 *
	 */
	public GetUserProfileResponse createGetUserProfileResponse() {
		return new GetUserProfileResponse();
	}

	/**
	 * Create an instance of {@link UStoreException }
	 *
	 */
	public UStoreException createUStoreException() {
		return new UStoreException();
	}

	/**
	 * Create an instance of {@link GetPurchasedServicesResponse }
	 *
	 */
	public GetPurchasedServicesResponse createGetPurchasedServicesResponse() {
		return new GetPurchasedServicesResponse();
	}

	/**
	 * Create an instance of {@link GetUpdatesForServices }
	 *
	 */
	public GetUpdatesForServices createGetUpdatesForServices() {
		return new GetUpdatesForServices();
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
	 * Create an instance of {@link GetPurchasedServices }
	 *
	 */
	public GetPurchasedServices createGetPurchasedServices() {
		return new GetPurchasedServices();
	}

	/**
	 * Create an instance of {@link PurchaseFreeServiceResponse }
	 *
	 */
	public PurchaseFreeServiceResponse createPurchaseFreeServiceResponse() {
		return new PurchaseFreeServiceResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link UStoreException
	 * }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "UStoreException")
	public JAXBElement<UStoreException> createUStoreException(UStoreException value) {
		return new JAXBElement<UStoreException>(_USTORE_Exception_QNAME, UStoreException.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetPurchasedServicesResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getPurchasedServicesResponse")
	public JAXBElement<GetPurchasedServicesResponse> createGetPurchasedServicesResponse(
			GetPurchasedServicesResponse value) {
		return new JAXBElement<GetPurchasedServicesResponse>(_GetPurchasedServicesResponse_QNAME,
				GetPurchasedServicesResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetUpdatesForServices }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getUpdatesForServices")
	public JAXBElement<GetUpdatesForServices> createGetUpdatesForServices(GetUpdatesForServices value) {
		return new JAXBElement<GetUpdatesForServices>(_GetUpdatesForServices_QNAME,
				GetUpdatesForServices.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetUserProfileResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getUserProfileResponse")
	public JAXBElement<GetUserProfileResponse> createGetUserProfileResponse(GetUserProfileResponse value) {
		return new JAXBElement<GetUserProfileResponse>(_GetUserProfileResponse_QNAME, GetUserProfileResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetSessionKeyResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getSessionKeyResponse")
	public JAXBElement<GetSessionKeyResponse> createGetSessionKeyResponse(GetSessionKeyResponse value) {
		return new JAXBElement<GetSessionKeyResponse>(_GetSessionKeyResponse_QNAME, GetSessionKeyResponse.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link RegisterDeployManagerResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "registerDeployManagerResponse")
	public JAXBElement<RegisterDeployManagerResponse> createRegisterDeployManagerResponse(
			RegisterDeployManagerResponse value) {
		return new JAXBElement<RegisterDeployManagerResponse>(_RegisterDeployManagerResponse_QNAME,
				RegisterDeployManagerResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetPurchasedServices }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getPurchasedServices")
	public JAXBElement<GetPurchasedServices> createGetPurchasedServices(GetPurchasedServices value) {
		return new JAXBElement<GetPurchasedServices>(_GetPurchasedServices_QNAME, GetPurchasedServices.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetUserProfile
	 * }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getUserProfile")
	public JAXBElement<GetUserProfile> createGetUserProfile(GetUserProfile value) {
		return new JAXBElement<GetUserProfile>(_GetUserProfile_QNAME, GetUserProfile.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetSessionKey
	 * }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getSessionKey")
	public JAXBElement<GetSessionKey> createGetSessionKey(GetSessionKey value) {
		return new JAXBElement<GetSessionKey>(_GetSessionKey_QNAME, GetSessionKey.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link PurchaseFreeServiceResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "purchaseFreeServiceResponse")
	public JAXBElement<PurchaseFreeServiceResponse> createPurchaseFreeServiceResponse(
			PurchaseFreeServiceResponse value) {
		return new JAXBElement<PurchaseFreeServiceResponse>(_PurchaseFreeServiceResponse_QNAME,
				PurchaseFreeServiceResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link PurchaseFreeService }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "purchaseFreeService")
	public JAXBElement<PurchaseFreeService> createPurchaseFreeService(PurchaseFreeService value) {
		return new JAXBElement<PurchaseFreeService>(_PurchaseFreeService_QNAME, PurchaseFreeService.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link RegisterDeployManager }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "registerDeployManager")
	public JAXBElement<RegisterDeployManager> createRegisterDeployManager(RegisterDeployManager value) {
		return new JAXBElement<RegisterDeployManager>(_RegisterDeployManager_QNAME, RegisterDeployManager.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetUpdatesForServicesResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getUpdatesForServicesResponse")
	public JAXBElement<GetUpdatesForServicesResponse> createGetUpdatesForServicesResponse(
			GetUpdatesForServicesResponse value) {
		return new JAXBElement<GetUpdatesForServicesResponse>(_GetUpdatesForServicesResponse_QNAME,
				GetUpdatesForServicesResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetFreeServices }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getFreeServices")
	public JAXBElement<GetFreeServices> createGetFreeServices(GetFreeServices value) {
		return new JAXBElement<GetFreeServices>(_GetFreeServices_QNAME, GetFreeServices.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetFreeServicesResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://tools.ustore.commerce.universaal.org/", name = "getFreeServicesResponse")
	public JAXBElement<GetFreeServicesResponse> createGetFreeServicesResponse(GetFreeServicesResponse value) {
		return new JAXBElement<GetFreeServicesResponse>(_GetFreeServicesResponse_QNAME,
				GetFreeServicesResponse.class, null, value);
	}

}
