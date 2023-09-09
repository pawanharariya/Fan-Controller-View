## Fan Controller ##

The Fan Controller app displays a circular UI element that resembles a physical fan control, to
control the fan speed. It demonstrates how to create custom views.

## App Preview ##

<img src="https://github.com/pawanharariya/Fan-Controller-View/assets/43620548/ad90b838-6b08-4b69-92d8-7fc1ca69e73a" width=180 alt="App Preview 1"/>
<img src="https://github.com/pawanharariya/Fan-Controller-View/assets/43620548/523b0cb6-695d-4216-a6a2-ff2b69cdabba" width=180 alt="App Preview 2"/>
<img src="https://github.com/pawanharariya/Fan-Controller-View/assets/43620548/a09f6401-bfa2-4a83-9c5a-fd9226ccf2de" width=180 alt="App Preview 3"/>
<img src="https://github.com/pawanharariya/Fan-Controller-View/assets/43620548/4e4c3b49-c9ea-445b-be39-72af9fac7600" width=180 alt="App Preview 4"/>

## Custom Views ##

The View class provides many subclasses called UI widgets for common use cases, like TextView,
ImageView, Button and EditText. We can create custom views by subclassing these views or directly
extend the View class to start from scratch. To save efforts we must extend the closest possible
existing view available. Steps for creating custom views :

- Extend View, or extend a View subclass.
- If a subclass is extended only override the behaviour or aspects that are needed to be changed.
- If the View class is extended, override `onDraw()`, `onMeasure()` and other methods to draw its
  shape and control its appearance.
- Add code to respond to user interaction and redraw if necessary.
- Use custom view as UI widget in layout.
- Define custom attributes for the view to provide customisation in different layouts.

## Drawing Custom Views ##

When we extend the view subclass such as EditText, that subclass defines view's appearance and
attributes and draws itself. We don't have to write the code to draw the view because we can
override methods of the parent to customize the view when required. But, if we create view from
scratch by extending view directly, we have to draw the entire view everytime the screen refreshes.
We need to override view methods to handle drawing. Following are the things to be done to draw
custom views:

1. Override `onSizeChanged()` to calculate the view's size when it appears first time and each time
   its size changes. It includes calculation for positions, dimensions, or any other value related
   to the custom view's size.

2. Override `onDraw()` to draw the custom view, using a Canvas object styled by a Paint object.

3. Call `invalidate()` when responding to a user click that changes how the view is drawn, forcing a
   call to `onDraw()` to redraw the view.

4. Override `onMeasure()` to accurately define how the custom view is aligned by parent view and
   fits into layout.

## View Interactivity ##

To enable custom view to be clickable following needs to be done:

- Set view's `isClickable` property to true.

- Implement `performClick()` to handle the click.

- Call `invalidate()` to redraw the view.

For standard view, we implement `onClickListener()` to handle the view clicks. For custom views we
instead implement `performClick()` method. The super class method also calls the click listener, so
we add default functions to perform click, and leave on-click listener available for further
customisation.

## Custom Attributes ##

We can make the custom view to have custom attributes. Following are the steps to achieve it.

- Create `res/values/attrs.xml` file, if it doesn't exist already.
- Declare a styleable and define the required attributes and their value types.
    ```
    <declare-styleable name="DialView">
        <attr name="fanColor" format="color" />
    </declare-styleable>
    ```

- Set the values to these attributes in the custom view, using `app` namespace.
    ```
    <com.example.fancontroller.DialView
        android:id="@+id/dial_view"
        app:fanColor="#FFEB3B"/>
    ```

- In order to use the attributes, we need to retrieve them. They are stored in an AttributeSet. We
  use `withStyledAttributes` extension function to initialise the attributes in our custom view
  class.
    ```
    context.withStyledAttributes(attrs, R.styleable.DialView) {
        fanSpeedColor = getColor(R.styleable.DialView_fanColor, 0)
    }
    ```

## Helpful Tips ##

1. `onDraw()` method is called every time the screen refreshes, which can be many times a second. To
   avoid visual glitches, and better performance, we should do as little work as possible
   in `onDraw()`. One way to do this is by avoiding allocations in `onDraw()`.

2. The `performClick()` method can `onClickListener()`. To do so we need to call super class method
   first, which enables accessibility events and calls onClickListener.

3. If something in the custom view changes for any reason, including user interaction, and the
   change needs to be displayed, we must call `invalidate()`, to invalidate the current view and to
   re-draw the view.