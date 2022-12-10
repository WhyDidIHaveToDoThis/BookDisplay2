# Book Display
Read books while moving!

Make written books usable so players can touch grass.

Read a book like a pamphlet or guide, use a book like a notepad.


## Mod Support
Book Display natively supports:
 - Bibliocraft
   - Big Writing Book
   - Clipboard
   - Recipe Book
   - Redstone: Volume I
   - Slotted Book
 - _Lexica Botania_ from Botania
 - _Engineer's Manual_ from Immersive Engineering
 - Any book from Mantle (e.g. _Materials and You_ from Tinkers' Construct)
 - _OpenComputers Manual_ from OpenComputers
 - Any book from Patchouli
 - The Akashic Tome
 - TIS-3D
   - TIS-3D Reference Manual
   - Code Bible

### Adding Your Own Book
To add your own book gui to the Book Display registry, you first need to add Book Display to your dev enviroment as a dependency.
 
```groovy
repositories {
    // ...
    maven {
        name 'Astavie' // You can name it whatever you want
        url 'https://raw.githubusercontent.com/Astavie/Astavie.github.io/mvn-repo/'
    }
}

dependencies {
    // ...
    compile 'astavie:bookdisplay:<version>:deobf'
}
```
#### Registering Your GUI
Next, you register your book gui with `BookDisplay.register(predicate, factory);`

##### predicate
A java 8 predicate, with an `ItemStack` as its input. This predicate should return `true` if the `ItemStack` opens your book gui on a right click.

##### factory
A java 8 function, with an `ItemStack` as its input and an `IBookWrapper` as its output (more on them later). Returns an instance of `IBookWrapper` using the `ItemStack` previously checked against.

#### Creating a Wrapper
`BookWrapper` is a class that transforms a `GuiScreen` instance into an overlay that can be drawn on the ingame gui. Its constructor has two parameters: `gui`, a new instance of your book gui, and `drawsBackground`, a boolean. If your GUI draws a grey background when its opened, you should put `true` here, so Book Display can remove the background in the overlay.

If you want to add page scrolling, you should override the `left()` and `right()` methods. These methods should switch the page, either to the left or right of the current page.

Sometimes, there are buttons in your GUI that you don't want to display on the overlay. In the `makeButtonInvisible(button)` method, you can choose per button which ones should be made invisible.

#### Creating a Custom Wrapper
`IBookWrapper` is an interface that contains rendering methods that are invoked when drawing the ingame gui. The class contains 5 extra methods you need to override, apart from `left()` and `right()`.

##### draw(stack, side, partialTicks)
This method should draw the gui on the left or right side of the screen as specified by the `side` parameter. If you want to use rendering methods from the vanilla `Gui` class, you can simply make your wrapper extend `Gui`.

##### setSize(width, height, side)
Invoked when the window resolution changes.

##### onOpen()
Invoked when the overlay is opened, before its been rendered.

##### onTick()
Invoked every tick while the overlay is opened.

##### onClose()
Invoked when the overlay is closed, after its been rendered.
