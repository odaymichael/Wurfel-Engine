object CreateMap: TCreateMap
  Left = 350
  Top = 249
  Width = 270
  Height = 190
  BorderIcons = [biSystemMenu, biMinimize]
  Caption = 'Create Map'
  Color = clBtnFace
  Constraints.MinHeight = 190
  Constraints.MinWidth = 270
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  Icon.Data = {
    0000010001002020100000000000E80200001600000028000000200000004000
    0000010004000000000080020000000000000000000000000000000000000000
    000000008000008000000080800080000000800080008080000080808000C0C0
    C0000000FF0000FF000000FFFF00FF000000FF00FF00FFFF0000FFFFFF000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000022000000000000000000000000000022222200000000000000000
    0000000222222222200000000000000000000222222222222220000000000000
    0002222222222222222220000000000002222222222222222222222000000002
    2222222222222222222222222000022222222222222222222222222222200002
    2222222222222222222222222000000002222222222222222222222000000000
    0002222222222222222220000000000000000222222222222220000000000000
    0000000222222222200000000000000000000000022222200000000000000000
    0000000000022000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    0000000000000000000000000000000000000000000000000000000000000000
    000000000000000000000000000000000000000000000000000000000000FFFF
    FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE7FFFFFF8
    1FFFFFE007FFFF8001FFFE00007FF800001FE000000780000001000000008000
    0001E0000007F800001FFE00007FFF8001FFFFE007FFFFF81FFFFFFE7FFFFFFF
    FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF}
  OldCreateOrder = False
  Position = poScreenCenter
  OnCreate = FormCreate
  DesignSize = (
    254
    155)
  PixelsPerInch = 96
  TextHeight = 13
  object btOK: TButton
    Left = 174
    Top = 125
    Width = 75
    Height = 25
    Anchors = [akRight, akBottom]
    Caption = 'OK'
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Verdana'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 3
    OnClick = btOKClick
  end
  object edPath: TEdit
    Left = 0
    Top = 100
    Width = 251
    Height = 24
    Anchors = [akLeft, akTop, akRight]
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Verdana'
    Font.Style = []
    ParentFont = False
    TabOrder = 2
    Text = 'Error'
  end
  object btPath: TButton
    Left = 0
    Top = 70
    Width = 91
    Height = 21
    Caption = 'Select path'
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Verdana'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 1
    OnClick = btPathClick
  end
  object laMapname: TLabeledEdit
    Left = 0
    Top = 30
    Width = 251
    Height = 24
    Anchors = [akLeft, akTop, akRight]
    EditLabel.Width = 78
    EditLabel.Height = 18
    EditLabel.Caption = 'Mapname'
    EditLabel.Font.Charset = ANSI_CHARSET
    EditLabel.Font.Color = clWindowText
    EditLabel.Font.Height = -16
    EditLabel.Font.Name = 'Verdana'
    EditLabel.Font.Style = []
    EditLabel.ParentFont = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Verdana'
    Font.Style = []
    LabelSpacing = 5
    MaxLength = 100
    ParentFont = False
    TabOrder = 0
    Text = 'Pleaseenteraname'
    OnChange = laMapnameChange
  end
end
