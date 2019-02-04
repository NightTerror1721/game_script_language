/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script;

/**
 *
 * @author Asus
 */
public interface ScriptConstants
{
    int MAGIC_NUMBER = ('G' & 0xff) | (('S' & 0xff) << 8) | (('L' & 0xff) << 16) | (('S' & 0xff) << 24);
    
    String SOURCE_FILE_EXTENSION = "gsl";
    String COMPILED_FILE_EXTENSION = "cgsl";
    
    int MAX_STACK_LENGTH = 256;
    int MAX_VARS = 256;
    
    int CODE_STACK_LEN = 0x0;
    int CODE_VARS_LEN  = 0x1;
    int CODE_INIT      = 0x2;
}
