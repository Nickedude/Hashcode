import Data.Char

data Cell = Tomato | Mushroom

data Pizza = Pizza [[Cell]] R C Min Max 

type Max = Int
type Min = Int
type R = Int
type C = Int

data Slices = Slices [((R,C),(R,C))]

test = readFile "pizza.txt"

pizzaParser :: FilePath -> IO Pizza
pizzaParser file = 
	do 
	text <- readFile file
	let (row : _ : col: _ : mi : _ : ma : _ : contents) = text
	let mi' = digitToInt mi
   	let ma' = digitToInt ma
    let rows = digitToInt row
    let col' = digitToInt col
    let cells =  pizzaParser' contents [[]]
	return (Pizza cells row' col' mi' ma')
	  where
    	pizzaParser' :: String -> [[Cell]] -> [[Cell]]
    	pizzaParser' ('\n':xs) ys = pizzaParser' xs (ys:[])
 