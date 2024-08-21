/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battaglianavale;

/**
 *
 * @author IlDivinoPase
 */
public class NumeroInsufficienteDiNaviException extends Exception
{
    public NumeroInsufficienteDiNaviException()
    {
        super("Il numero di navi nel pachetto navi personalizzato é insufficiente");
    }
    
    public NumeroInsufficienteDiNaviException(String message)
    {
        super(message);
    }
}
