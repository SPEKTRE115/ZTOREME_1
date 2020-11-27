from behave import given, when, then
import time

@given(u'que ingreso al sistema')
def step_impl(context):
    pass


@given(u'le doy click a agregar producto')
def step_impl(context):
    time.sleep(0.5)
    context.driver.find_element_by_id('com.example.ztoreme_1:id/seccion2').click()

@given(u'ingreso el nombre del producto "Televisión"')
def step_impl(context):
    raise NotImplementedError(u'STEP: Given ingreso el nombre del producto "Televisión"')


@given(u'ingreso la cantidad "7", el stock mínimo "3" y el stock máximo "10"')
def step_impl(context):
    raise NotImplementedError(u'STEP: Given ingreso la cantidad "7", el stock mínimo "3" y el stock máximo "10"')


@given(u'ingreso el precio de compra "5000.00" y el precio de venta "10000.00"')
def step_impl(context):
    raise NotImplementedError(u'STEP: Given ingreso el precio de compra "5000.00" y el precio de venta "10000.00"')


@when(u'le doy click en guardar, y acepto guardar el producto')
def step_impl(context):
    raise NotImplementedError(u'STEP: When le doy click en guardar, y acepto guardar el producto')


@then(u'me muestra el mensaje producto agregado, y se agrega a la base de datos.')
def step_impl(context):
    raise NotImplementedError(u'STEP: Then me muestra el mensaje producto agregado, y se agrega a la base de datos.')