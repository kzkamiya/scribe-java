package org.scribe.builder.api;


import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class EagleApi extends DefaultApi20
{
  private static final String AUTHORIZE_URL = "http://openam.test.aomai.jp/openam/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
  
  /**
   * Returns the verb for the access token endpoint (defaults to GET)
   * 
   * @return access token endpoint verb
   */
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }
  
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

  @Override
  public String getAccessTokenEndpoint()
  {
	  	  
		return "http://openam.test.aomai.jp/openam/oauth2/access_token?grant_type=authorization_code";
//		return "http://openam.test.aomai.jp/openam/oauth2/access_token?grant_type=password";
//		return "http://openam.test.aomai.jp/openam/oauth2/access_token?grant_type=refresh_token";
//		return "http://openam.test.aomai.jp/openam/oauth2/access_token?grant_type=urn:ietf:params:oauth:grant-type:saml2-bearer";
//		return "http://openam.test.aomai.jp/openam/oauth2/access_token?grant_type=client_credentials";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");

    // Append scope if present
    if(config.hasScope())
    {
     return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
    }
    else
    {
      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
    }
  }
}
