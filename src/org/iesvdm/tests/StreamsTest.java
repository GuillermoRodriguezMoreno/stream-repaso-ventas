package org.iesvdm.tests;

import static org.junit.jupiter.api.Assertions.fail;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.iesvdm.streams.Cliente;
import org.iesvdm.streams.ClienteHome;
import org.iesvdm.streams.Comercial;
import org.iesvdm.streams.ComercialHome;
import org.iesvdm.streams.Pedido;
import org.iesvdm.streams.PedidoHome;
import org.junit.jupiter.api.Test;

class StreamsTest {

	@Test
	void test() {
		fail("Not yet implemented");
	}


	@Test
	void testSkeletonCliente() {
	
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			list.forEach(System.out::println);
		
			
			//TODO STREAMS
			
		
			cliHome.commitTransaction();
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	

	@Test
	void testSkeletonComercial() {
	
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			list.forEach(System.out::println);		
			//TODO STREAMS
		
			comHome.commitTransaction();
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	@Test
	void testSkeletonPedido() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
			list.forEach(System.out::println);	
						
			//TODO STREAMS
		
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	/**
	 * 1. Devuelve un listado de todos los pedidos que se realizaron durante el año 2017, 
	 * cuya cantidad total sea superior a 500€.
	 * @throws ParseException 
	 */
	@Test
	void test1() throws ParseException {
		
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
			
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2016 = sdf.parse("2016-12-31");
			
			List<Pedido> list = pedHome.findAll();
				
			//TODO STREAMS
			List<Pedido> newList = list.stream().
												filter(ped -> ped.getFecha().after(ultimoDia2016) && ped.getTotal() > 500)
												.collect(toList());

			newList.forEach(System.out::println);
						
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 2. Devuelve un listado con los identificadores de los clientes que NO han realizado algún pedido. 
	 * 
	 */
	@Test
	void test2() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			//TODO STREAMS		

			List<Integer> clientesSinPedidos = clientes.stream()
					.filter(cliente -> pedidos.stream().noneMatch(pedido -> pedido.getIdCliente() == cliente.getId()))
					.map(Cliente::getId)
					.collect(Collectors.toList());
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 3. Devuelve el valor de la comisión de mayor valor que existe en la tabla comercial
	 */
	@Test
	void test3() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			
			//TODO STREAMS		
			Optional<Double> comisionMaxima = comerciales.stream()
					.map(Comercial::getComision)
					.max(Double::compare);
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 4. Devuelve el identificador, nombre y primer apellido de aquellos clientes cuyo segundo apellido no es NULL.
	 * El listado deberá estar ordenado alfabéticamente por apellidos y nombre.
	 */
	@Test
	void test4() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			//TODO STREAMS
			List<Cliente> clientesConSegundoApellido = clientes.stream()
					.filter(cliente -> cliente.getApellido2() != null)
					.sorted(Comparator.comparing(Cliente::getApellido1)
							.thenComparing(Cliente::getNombre))
					.collect(Collectors.toList());

			clientesConSegundoApellido.forEach(cliente -> System.out.println(
					"ID: " + cliente.getId() +
							", Nombre: " + cliente.getNombre() +
							", Apellido1: " + cliente.getApellido1()));
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 5. Devuelve un listado con los nombres de los comerciales que terminan por "el" o "o". 
	 *  Tenga en cuenta que se deberán eliminar los nombres repetidos.
	 */
	@Test
	void test5() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
			comHome.beginTransaction();
		
			List<Comercial> list = comHome.findAll();		
			
			//TODO STREAMS
			List<String> nombresComercialesFiltrados = comerciales.stream()
					.map(Comercial::getNombre)
					.filter(nombre -> nombre.endsWith("el") || nombre.endsWith("o"))
					.distinct()
					.collect(Collectors.toList());
			comHome.commitTransaction();
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 6. Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017, cuya cantidad esté entre 300 € y 1000 €.
	 */
	@Test
	void test6() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			//TODO STREAMS
			List<Cliente> clientesConPedidos2017 = pedidos.stream()
					.filter(pedido -> pedido.getFecha().getYear() == 2017 && pedido.getTotal() >= 300 && pedido.getTotal() <= 1000)
					.map(pedido -> clientes.stream().filter(cliente -> cliente.getId() == pedido.getIdCliente()).findFirst().orElse(null))
					.filter(cliente -> cliente != null)
					.distinct()
					.collect(Collectors.toList());
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 7. Calcula la media del campo total de todos los pedidos realizados por el comercial Daniel Sáez
	 */
	@Test
	void test7() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			//TODO STREAMS

			OptionalDouble mediaPedidosDanielSaez = pedidos.stream()
					.filter(pedido -> pedido.getComercial().getNombre().equals(nombreComercial))
					.mapToDouble(Pedido::getTotal)
					.average();
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 8. Devuelve un listado con todos los pedidos que se han realizado.
	 *  Los pedidos deben estar ordenados por la fecha de realización
	 * , mostrando en primer lugar los pedidos más recientes
	 */
	@Test
	void test8() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			//TODO STREAMS

			List<Pedido> pedidosOrdenadosPorFecha = pedidos.stream()
					.sorted((pedido1, pedido2) -> pedido2.getFecha().compareTo(pedido1.getFecha()))
					.collect(Collectors.toList());
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 9. Devuelve todos los datos de los dos pedidos de mayor valor.
	 */
	@Test
	void test9() {
	
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			//TODO STREAMS
			List<Pedido> dosPedidosDeMayorValor = pedidos.stream()
					.sorted(Comparator.comparingDouble(Pedido::getTotal).reversed())
					.limit(2)
					.collect(Collectors.toList());
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 10. Devuelve un listado con los identificadores de los clientes que han realizado algún pedido. 
	 * Tenga en cuenta que no debe mostrar identificadores que estén repetidos.
	 */
	@Test
	void test10() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			
			//TODO STREAMS
			List<Integer> clientesConPedidos = pedidos.stream()
					.map(Pedido::getIdCliente)
					.distinct()
					.collect(Collectors.toList())
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 11. Devuelve un listado con el nombre y los apellidos de los comerciales que tienen una comisión entre 0.05 y 0.11.
	 * 
	 */
	@Test
	void test11() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			//TODO STREAMS
			List<String> comercialesConComisionEntre05y11 = comerciales.stream()
					.filter(comercial -> comercial.getComision() >= 0.05 && comercial.getComision() <= 0.11)
					.map(comercial -> comercial.getNombre() + " " + comercial.getApellido1() + " " + comercial.getApellido2())
					.collect(Collectors.toList());
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 12. Devuelve el valor de la comisión de menor valor que existe para los comerciales.
	 * 
	 */
	@Test
	void test12() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			//TODO STREAMS

			OptionalDouble comisionMinima = comerciales.stream()
					.mapToDouble(Comercial::getComision)
					.min();
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 13. Devuelve un listado de los nombres de los clientes que 
	 * empiezan por A y terminan por n y también los nombres que empiezan por P. 
	 * El listado deberá estar ordenado alfabéticamente.
	 * 
	 */
	@Test
	void test13() {
		
		ComercialHome comHome = new ComercialHome();	
		//PedidoHome pedHome = new PedidoHome();	
		try {
			//pedHome.beginTransaction();
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
			
			//TODO STREAMS

			List<String> nombresClientesFiltrados = clientes.stream()
					.filter(cliente -> (cliente.getNombre().startsWith("A") && cliente.getNombre().endsWith("n"))
							|| cliente.getNombre().startsWith("P"))
					.map(Cliente::getNombre)
					.sorted()
					.collect(Collectors.toList());

			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 14. Devuelve un listado de los nombres de los clientes 
	 * que empiezan por A y terminan por n y también los nombres que empiezan por P. 
	 * El listado deberá estar ordenado alfabéticamente.
	 */
	@Test
	void test14() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			//TODO STREAMS

			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 15. Devuelve un listado de los clientes cuyo nombre no empieza por A. 
	 * El listado deberá estar ordenado alfabéticamente por nombre y apellidos.
	 */
	@Test
	void test15() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			//TODO STREAMS
			List<Cliente> clientesNoEmpiezanPorA = clientes.stream()
					.filter(cliente -> !cliente.getNombre().startsWith("A"))
					.sorted(Comparator.comparing(Cliente::getNombre)
							.thenComparing(Cliente::getApellido1)
							.thenComparing(Cliente::getApellido2))
					.collect(Collectors.toList());
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 16. Devuelve un listado con el identificador, nombre y los apellidos de todos 
	 * los clientes que han realizado algún pedido. 
	 * El listado debe estar ordenado alfabéticamente por apellidos y nombre y se deben eliminar los elementos repetidos.
	 */
	@Test
	void test16() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			
			//TODO STREAMS
			List<Cliente> clientesConPedidos = pedidos.stream()
					.map(pedido -> clientes.stream().filter(cliente -> cliente.getId() == pedido.getIdCliente()).findFirst().orElse(null))
					.filter(cliente -> cliente != null)
					.distinct()
					.sorted(Comparator.comparing(Cliente::getApellido1)
							.thenComparing(Cliente::getApellido2)
							.thenComparing(Cliente::getNombre))
					.collect(Collectors.toList());
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 17. Devuelve un listado que muestre todos los pedidos que ha realizado cada cliente. 
	 * El resultado debe mostrar todos los datos del cliente primero junto con un sublistado de sus pedidos. 
	 * El listado debe mostrar los datos de los clientes ordenados alfabéticamente por nombre y apellidos.
	 * 
Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100]
	Pedido [id=2, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=270.65, fecha=2016-09-10]
	Pedido [id=16, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=2389.23, fecha=2019-03-11]
	Pedido [id=15, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=370.85, fecha=2019-03-11]
Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200]
	Pedido [id=12, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=3045.6, fecha=2017-04-25]
	Pedido [id=7, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=5760.0, fecha=2015-09-10]
	Pedido [id=3, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=65.26, fecha=2017-10-05]
	...
	 */
	@Test
	void test17() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			//TODO STREAMS
			Map<Cliente, List<Pedido>> pedidosPorCliente = pedidos.stream()
					.map(pedido -> clientes.stream().filter(cliente -> cliente.getId() == pedido.getIdCliente()).findFirst().orElse(null))
					.filter(cliente -> cliente != null)
					.collect(Collectors.groupingBy(cliente -> cliente,
							Collectors.mapping(pedido -> pedido, Collectors.toList())));

			pedidosPorCliente.entrySet().stream()
					.sorted(Comparator.comparing(entry -> entry.getKey().getNombre() + " " +
							entry.getKey().getApellido1() + " " + entry.getKey().getApellido2()))
					.forEach(entry -> {
						System.out.println(entry.getKey());
						entry.getValue().forEach(pedido -> System.out.println("\t" + pedido));
					});
			
			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 18. Devuelve un listado que muestre todos los pedidos en los que ha participado un comercial. 
	 * El resultado debe mostrar todos los datos de los comerciales y el sublistado de pedidos. 
	 * El listado debe mostrar los datos de los comerciales ordenados alfabéticamente por apellidos.
	 */
	@Test
	void test18() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
		
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();

			Map<Comercial, List<Pedido>> pedidosPorComercial = pedidos.stream()
					.map(Pedido::getComercial)
					.filter(comercial -> comercial != null)
					.collect(Collectors.groupingBy(comercial -> comercial,
							Collectors.mapping(pedido -> pedido, Collectors.toList())));

			pedidosPorComercial.entrySet().stream()
					.sorted(Comparator.comparing(entry -> entry.getKey().getApellido1() + " " + entry.getKey().getApellido2()))
					.forEach(entry -> {
						System.out.println(entry.getKey());
						entry.getValue().forEach(pedido -> System.out.println("\t" + pedido));
					});
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 19. Devuelve el nombre y los apellidos de todos los comerciales que ha participado 
	 * en algún pedido realizado por María Santana Moreno.
	 */
	@Test
	void test19() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			
			//TODO STREAMS

			List<String> comercialesDeMaria = pedidos.stream()
					.filter(pedido -> pedido.getCliente().getNombre().equals(nombreClienteBuscado))
					.map(pedido -> pedido.getComercial().getNombre() + " " +
							pedido.getComercial().getApellido1() + " " +
							pedido.getComercial().getApellido2())
					.distinct()
					.collect(Collectors.toList());
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	

	/**
	 * 20. Devuelve un listado que solamente muestre los comerciales que no han realizado ningún pedido.
	 */
	@Test
	void test20() {
		
		ComercialHome comHome = new ComercialHome();	
		try {
		
			comHome.beginTransaction();
			
			List<Comercial> list = comHome.findAll();		
		
			//TODO STREAMS

			List<Comercial> comercialesSinPedidos = comerciales.stream()
					.filter(comercial -> pedidos.stream().noneMatch(pedido -> pedido.getComercial().equals(comercial)))
					.collect(Collectors.toList());
			comHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			comHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	/**
	 * 21. Calcula el número total de comerciales distintos que aparecen en la tabla pedido
	 */
	@Test
	void test21() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			//TODO STREAMS
			List<Comercial> comercialesDistintos = pedidos.stream()
					.map(Pedido::getComercial)
					.distinct()
					.collect(Collectors.toList());
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 * 22. Calcula el máximo y el mínimo de total de pedido en un solo stream, transforma el pedido a un array de 2 double total, utiliza reduce junto con el array de double para calcular ambos valores.
	 */
	@Test
	void test22() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			//TODO STREAMS

			double[] maxMinTotal = pedidos.stream()
					.mapToDouble(Pedido::getTotal)
					.reduce(new double[]{Double.MIN_VALUE, Double.MAX_VALUE},
							(acc, total) -> new double[]{Math.max(acc[0], total), Math.min(acc[1], total)});

			System.out.println("Máximo total: " + maxMinTotal[0]);
			System.out.println("Mínimo total: " + maxMinTotal[1]);
			
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 * 23. Calcula cuál es el valor máximo de categoría para cada una de las ciudades que aparece en cliente
	 */
	@Test
	void test23() {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			
			//TODO STREAMS
			Map<String, Integer> maxCategoriaPorCiudad = clientes.stream()
					.collect(Collectors.groupingBy(Cliente::getCiudad,
							Collectors.mapping(Cliente::getCategoria, Collectors.maxBy(Integer::compareTo))))
					.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().orElse(0)));


			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 24. Calcula cuál es el máximo valor de los pedidos realizados 
	 * durante el mismo día para cada uno de los clientes. Es decir, el mismo cliente puede haber 
	 * realizado varios pedidos de diferentes cantidades el mismo día. Se pide que se calcule cuál es 
	 * el pedido de máximo valor para cada uno de los días en los que un cliente ha realizado un pedido. 
	 * Muestra el identificador del cliente, nombre, apellidos, la fecha y el valor de la cantidad.
	 * Pista: utiliza collect, groupingBy, maxBy y comparingDouble métodos estáticos de la clase Collectors
	 */
	@Test
	void test24() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
						
			//TODO STREAMS
			Map<String, Pedido> maxPedidoPorClienteYFecha = pedidos.stream()
					.collect(Collectors.groupingBy(pedido ->
									pedido.getCliente().getId() + "_" + pedido.getFecha(),
							Collectors.maxBy(Comparator.comparingDouble(Pedido::getTotal))));

			maxPedidoPorClienteYFecha.values().forEach(pedido -> {
				System.out.println("Cliente ID: " + pedido.getCliente().getId() +
						", Nombre: " + pedido.getCliente().getNombre() +
						", Apellidos: " + pedido.getCliente().getApellido1() + " " + pedido.getCliente().getApellido2() +
						", Fecha: " + pedido.getFecha() +
						", Valor del pedido máximo: " + pedido.getTotal());
			});
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 *  25. Calcula cuál es el máximo valor de los pedidos realizados durante el mismo día para cada uno de los clientes, 
	 *  teniendo en cuenta que sólo queremos mostrar aquellos pedidos que superen la cantidad de 2000 €.
	 *  Pista: utiliza collect, groupingBy, filtering, maxBy y comparingDouble métodos estáticos de la clase Collectors
	 */
	@Test
	void test25() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			//TODO STREAMS

			Map<String, Pedido> maxPedidoPorClienteYFecha = pedidos.stream()
					.filter(pedido -> pedido.getTotal() > 2000)
					.collect(Collectors.groupingBy(pedido ->
									pedido.getCliente().getId() + "_" + pedido.getFecha(),
							Collectors.collectingAndThen(
									Collectors.maxBy(Comparator.comparingDouble(Pedido::getTotal)),
									Optional::orElse(null)
                )
        ));

			maxPedidoPorClienteYFecha.values().forEach(pedido -> {
				System.out.println("Cliente ID: " + pedido.getCliente().getId() +
						", Nombre: " + pedido.getCliente().getNombre() +
						", Apellidos: " + pedido.getCliente().getApellido1() + " " + pedido.getCliente().getApellido2() +
						", Fecha: " + pedido.getFecha() +
						", Valor del pedido máximo (> 2000 €): " + pedido.getTotal());
			});
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 *  26. Devuelve un listado con el identificador de cliente, nombre y apellidos 
	 *  y el número total de pedidos que ha realizado cada uno de clientes durante el año 2017.
	 * @throws ParseException 
	 */
	@Test
	void test26() throws ParseException {
		
		ClienteHome cliHome = new ClienteHome();
		
		try {
			cliHome.beginTransaction();
	
			List<Cliente> list = cliHome.findAll();
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2016 = sdf.parse("2016-12-31");
			Date primerDia2018 = sdf.parse("2018-01-01");
			
			//TODO STREAMS

			List<ClienteConTotalPedidos> clientesConTotalPedidos2017 = pedidos.stream()
					.filter(pedido -> pedido.getFecha().startsWith("2017"))
					.collect(Collectors.groupingBy(
							pedido -> pedido.getCliente(),
							Collectors.counting()
					))
					.entrySet().stream()
					.map(entry -> new ClienteConTotalPedidos(
							entry.getKey().getId(),
							entry.getKey().getNombre(),
							entry.getKey().getApellido1(),
							entry.getKey().getApellido2(),
							entry.getValue().intValue()
					))
					.collect(Collectors.toList());

			clientesConTotalPedidos2017.forEach(cliente -> System.out.println(
					"ID: " + cliente.getId() +
							", Nombre: " + cliente.getNombre() +
							", Apellidos: " + cliente.getApellido1() + " " + cliente.getApellido2() +
							", Número total de pedidos en 2017: " + cliente.getTotalPedidos()));

			cliHome.commitTransaction();
			
		}
		catch (RuntimeException e) {
			cliHome.rollbackTransaction();
		    throw e; // or display error message
		}
		
	}
	
	
	/**
	 * 27. Devuelve cuál ha sido el pedido de máximo valor que se ha realizado cada año. El listado debe mostrarse ordenado por año.
	 */
	@Test
	void test27() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			Calendar calendar = Calendar.getInstance();
			
			
			//TODO STREAMS
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


			List<Pedido> maxPedidoPorAnio = pedidos.stream()
					.collect(Collectors.groupingBy(
							pedido -> {
								try {
									return dateFormat.parse(pedido.getFecha()).getYear() + 1900;
								} catch (ParseException e) {
									e.printStackTrace();
									return 0;
								}
							},
							Collectors.collectingAndThen(
									Collectors.maxBy(Comparator.comparingDouble(Pedido::getTotal)),
									Optional::orElse(null)
                )
        ))
        .entrySet().stream()
					.sorted(Map.Entry.comparingByKey())
					.map(Map.Entry::getValue)
					.collect(Collectors.toList());

			maxPedidoPorAnio.forEach(pedido -> System.out.println(
					"Año: " + getAnio(pedido.getFecha()) +
							", Pedido de máximo valor: " + pedido));
					
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 *  28. Devuelve el número total de pedidos que se han realizado cada año.
	 */
	@Test
	void test28() {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			Calendar calendar = Calendar.getInstance();
			
			//TODO STREAMS

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Map<Integer, Long> pedidosPorAnio = pedidos.stream()
					.collect(Collectors.groupingBy(
							pedido -> {
								try {
									return dateFormat.parse(pedido.getFecha()).getYear() + 1900;
								} catch (ParseException e) {
									e.printStackTrace();
									return 0;
								}
							},
							Collectors.counting()
					));


			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	/**
	 *  29. Devuelve los datos del cliente que realizó el pedido
	 *  
	 *   más caro en el año 2019.
	 * @throws ParseException 
	 */
	@Test
	void test29() throws ParseException {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
					
			//PISTA: Generación por sdf de fechas
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date ultimoDia2018 = sdf.parse("2018-12-31");
			Date primerDia2020 = sdf.parse("2020-01-01");
			
			//TODO STREAMS
			Optional<Cliente> clientePedidoMasCaro2019 = pedidos.stream()
					.filter(pedido -> pedido.getFecha().startsWith("2019"))
					.max(Comparator.comparingDouble(Pedido::getTotal))
					.map(Pedido::getCliente);

			clientePedidoMasCaro2019.ifPresent(cliente -> System.out.println(
					"Cliente con pedido más caro en 2019: " +
							"ID: " + cliente.getId() +
							", Nombre: " + cliente.getNombre() +
							", Apellidos: " + cliente.getApellido1() + " " + cliente.getApellido2()));
				
			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
	
	/**
	 *  30. Calcula la estadísticas de total de todos los pedidos.
	 *  Pista: utiliza collect con summarizingDouble
	 */
	@Test
	void test30() throws ParseException {
		
		PedidoHome pedHome = new PedidoHome();	
		try {
			pedHome.beginTransaction();
		
			List<Pedido> list = pedHome.findAll();
			
			//TODO STREAMS
			DoubleSummaryStatistics estadisticasTotales = pedidos.stream()
					.collect(Collectors.summarizingDouble(Pedido::getTotal));

			System.out.println("Estadísticas del total de todos los pedidos:");
			System.out.println("Suma: " + estadisticasTotales.getSum());
			System.out.println("Promedio: " + estadisticasTotales.getAverage());
			System.out.println("Máximo: " + estadisticasTotales.getMax());
			System.out.println("Mínimo: " + estadisticasTotales.getMin());
			System.out.println("Número de pedidos: " + estadisticasTotales.getCount());

			pedHome.commitTransaction();
		}
		catch (RuntimeException e) {
			pedHome.rollbackTransaction();
		    throw e; // or display error message
		}
	}
	
}
