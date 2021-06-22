#!/bin/bash
rm -r src/autogen
mkdir -p src/autogen/
cp -r ../src/main/* src/autogen/
mv src/autogen/resources/cocoainput.mixins.json src/autogen/java
cd src/autogen/java
find . -type f -print0 | xargs -0 sed -i -e 's/gui\/screen\/EditSign/gui\/screens\/inventory\/SignEdit/g'
find . -type f -print0 | xargs -0 sed -i -e 's/gui\/screen\/EditBook/gui\/screens\/inventory\/BookEdit/g'
find . -type f -print0 | xargs -0 sed -i -e 's/gui.screen\./gui.screens\./g'
find . -type f -print0 | xargs -0 sed -i -e 's/screens.EditSignScreen/screens.inventory.SignEditScreen/g'
find . -type f -print0 | xargs -0 sed -i -e 's/screens.EditBookScreen/screens.inventory.BookEditScreen/g'
find . -type f -print0 | xargs -0 sed -i -e 's/EditSignScreen/SignEditScreen/g'
find . -type f -print0 | xargs -0 sed -i -e 's/EditBookScreen/BookEditScreen/g'
find . -type f -print0 | xargs -0 sed -i -e 's/widget/components/g'
find . -type f -print0 | xargs -0 sed -i -e 's/TextFieldWidget/EditBox/g'
find . -type f -print0 | xargs -0 sed -i -e 's/.util.text./.network.chat./g'
find . -type f -print0 | xargs -0 sed -i -e 's/StringTextComponent/TextComponent/g'
find . -type f -print0 | xargs -0 sed -i -e 's/FontRenderer/Font/g'
find . -type f -print0 | xargs -0 sed -i -e 's/util.Shared/Shared/g'
find . -type f -print0 | xargs -0 sed -i -e 's/network.chat.CharacterManager/client.StringSplitter/g'
find . -type f -print0 | xargs -0 sed -i -e 's/CharacterManager/StringSplitter/g'
find . -type f -print0 | xargs -0 sed -i -e 's/ITextProperties/FormattedText/g'
find . -type f -print0 | xargs -0 sed -i -e 's/ITextAcceptor/ContentConsumer/g'
find . -type f -print0 | xargs -0 sed -i -e 's/getMessage(owner.line)/getMessage(owner.line,false)/g'
find . -type f -print0 | xargs -0 sed -i -e 's/"refmap":"cocoainput.refmap.json",//g'
find . -type f -print0 | xargs -0 sed -i -e 's/net.minecraft.block.StandingSignBlock/net.minecraft.world.level.block.StandingSignBlock/g'
mv jp/axer/cocoainput/mixin/EditSignScreenMixin.java jp/axer/cocoainput/mixin/SignEditScreenMixin.java
mv jp/axer/cocoainput/mixin/EditBookScreenMixin.java jp/axer/cocoainput/mixin/BookEditScreenMixin.java
mv jp/axer/cocoainput/mixin/TextFieldWidgetMixin.java jp/axer/cocoainput/mixin/EditBoxMixin.java
mv jp/axer/cocoainput/wrapper/EditSignScreenWrapper.java jp/axer/cocoainput/wrapper/SignEditScreenWrapper.java
mv jp/axer/cocoainput/wrapper/EditBookScreenWrapper.java jp/axer/cocoainput/wrapper/BookEditScreenWrapper.java
mv jp/axer/cocoainput/wrapper/TextFieldWidgetWrapper.java jp/axer/cocoainput/wrapper/EditBoxWrapper.java
mv cocoainput.mixins.json ../resources


