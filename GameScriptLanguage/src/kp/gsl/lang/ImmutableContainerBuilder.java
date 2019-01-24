/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

/**
 *
 * @author Asus
 */
public interface ImmutableContainerBuilder<E, C>
{
    ImmutableContainerBuilder<E, C> addElement(E element);
    C build();
}
