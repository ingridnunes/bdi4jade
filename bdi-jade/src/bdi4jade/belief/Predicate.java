//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.belief;

/**
 * This interface represents a belief that is a logic predicate. The information
 * is represented as an object of type K, which has a boolean value to indicate
 * if the formula is true or false. Null may represents the absence of knowledge
 * if the formula is true or false.
 * 
 * @param <K>
 *            the type of the propositional statement.
 * 
 * @author Ingrid Nunes
 */
public interface Predicate<K> extends Belief<K, Boolean> {

}
