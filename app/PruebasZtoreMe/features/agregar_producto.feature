Característica: Agregar producto

	Como usuario
	 requiero guardar un producto en el sistema
	 para poder mantener registro de mi inventario

	Escenario: Producto valido
		Dado que ingreso al sistema
		Y le doy click a agregar producto
		Y ingreso el nombre del producto "Televisión"
		Y ingreso la cantidad "7", el stock mínimo "3" y el stock máximo "10"
		Y ingreso el precio de compra "5000.00" y el precio de venta "10000.00"
		Cuando le doy click en guardar, y acepto guardar el producto
		Entonces me muestra el mensaje producto agregado, y se agrega a la base de datos.
