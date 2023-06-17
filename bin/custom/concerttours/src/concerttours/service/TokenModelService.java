package concerttours.service;

import concerttours.model.TokenModel;


public interface TokenModelService
{
   TokenModel getToken();
   void saveToken(String token);
}
