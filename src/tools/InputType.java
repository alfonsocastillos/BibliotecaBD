package tools;

import javax.swing.text.PlainDocument;

/**
 * Condiciones para la entrada de texto en un campo.
 * @author carlos
 */
public class InputType extends PlainDocument{
    int tipo;                           // Tipo de dato.
    int tamanio;                        // Tamaño de la cadena.
    boolean conPunto = false;           // Para permitir o no punto en numericos.
    boolean conEspacio = false;         // para permitir o no espacios.
    boolean conMenos = false;           // Para el signo de menos.  
    
    // Tipos de datos permitidos (enum).
    public static int TIPO_HEXADEC = 0;
    public static int TIPO_SOLO_NUMEROS = 1;
    public static int TIPO_SOLO_LETRAS = 2;    

    /**
     * Constructor para hexadecimal y solo letras.
     * @param tipo Tipo de dato.
     * @param tamanio Tamanio maximo permitido.
     * @param conEspacio Permite tener espacios.
     */
    public InputType(int tipo, int tamanio, boolean conEspacio) {        
        this.tipo = tipo;
        this.tamanio = tamanio;
        this.conEspacio = conEspacio;
    }
    
    /**
     * Constructor para solo numeros.
     * @param tipo Tipo de dato.
     * @param tamanio Tamanio maximo permitido.
     * @param conEspacio Permite tener espacios.
     * @param conPunto Permite tener puntos decimales.
     */
    public InputType(int tipo, int tamanio, boolean conEspacio, boolean conPunto) {        
        this.tipo = tipo;
        this.tamanio = tamanio;
        this.conPunto = conPunto;
        this.conEspacio = conEspacio;
    }
    
    /**
     * Constructor para solo numeros, negativos.
     * @param tipo Tipo de dato.
     * @param tamanio Tamanio maximo permitido.
     * @param conEspacio Permite tener espacios.
     * @param conPunto Permite tener puntos decimales.
     * @param conMenos Permite signos.
     */
    public InputType(int tipo, int tamanio, boolean conEspacio, boolean conPunto, boolean conMenos) {        
        this.tipo = tipo;
        this.tamanio = tamanio;
        this.conPunto = conPunto;
        this.conEspacio = conEspacio;
        this.conMenos = conMenos;
    }
}    