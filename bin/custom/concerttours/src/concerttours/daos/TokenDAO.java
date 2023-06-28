package concerttours.daos;

import concerttours.model.TokenModel;

import java.util.List;

/**
 * An interface for the Band DAO including various operations for retrieving persisted Band model objects
 */
public interface TokenDAO
{
    TokenModel findToken();
}
